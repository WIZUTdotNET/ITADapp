package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dotnet.main.auth.JwtProvider;
import pl.dotnet.main.dao.model.NotificationEmail;
import pl.dotnet.main.dao.model.ResetPasswordToken;
import pl.dotnet.main.dao.model.User;
import pl.dotnet.main.dao.model.VerificationToken;
import pl.dotnet.main.dao.repository.ResetPasswordTokenRepository;
import pl.dotnet.main.dao.repository.UserRepository;
import pl.dotnet.main.dao.repository.VerificationTokenRepository;
import pl.dotnet.main.dto.Security.AuthenticationResponseDTO;
import pl.dotnet.main.dto.Security.LoginRequestDTO;
import pl.dotnet.main.dto.Security.RefreshTokenRequestDTO;
import pl.dotnet.main.dto.Security.RegisterRequestDTO;
import pl.dotnet.main.expections.ConnectException;
import pl.dotnet.main.expections.NotFoundRequestException;
import pl.dotnet.main.expections.UnauthorizedRequestException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    public ResponseEntity<String> signup(RegisterRequestDTO registerRequestDTO) {
        User user = User.builder()
                .userUUID(UUID.randomUUID())
                .username(registerRequestDTO.getUsername())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .surname(registerRequestDTO.getSurname())
                .name(registerRequestDTO.getName())
                .created(Instant.now())
                .isActive(false)
                .build();

        if (userService.validateUser(user)) {
            userRepository.save(user);

            String token = generateVerificationToken(user);

            mailService.sendMail(new NotificationEmail("Potwierdzenie rejestracji do aplikacji",
                    user.getEmail(), "Aby aktywować konto kliknij w poniższy link:</b>" +
                    "Frontend: http://localhost:3000/accountVerification/" + token + "</b>" +
                    "Backend: http://localhost:8080/api/auth/accountVerification/" + token));
            return new ResponseEntity<>("User Registration Successful", OK);
        }

        return new ResponseEntity<>("Username or email taken", EXPECTATION_FAILED);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public ResponseEntity<String> verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken.isEmpty()) {
            return new ResponseEntity<>("Account already activated or token not found", NOT_FOUND);
        }

        fetchUserAndEnable(verificationToken.orElseThrow(() -> new ConnectException("Invalid Token")));
        verificationTokenRepository.deleteById(verificationToken.get().getId());
        return new ResponseEntity<>("Account Activated Successfully", OK);
    }

    void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ConnectException("User not found with name - " + username));
        user.setIsActive(true);
        userRepository.save(user);
    }

    public AuthenticationResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(),
                loginRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        User user = userRepository.findByUsername(loginRequestDTO.getUsername()).orElseThrow(() -> new NotFoundRequestException("User not found"));
        return AuthenticationResponseDTO.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()).toString())
                .username(user.getUsername())
                .userId(user.getUserId())
                .uuid(user.getUserUUID())
                .build();
    }

    public AuthenticationResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        refreshTokenService.validateRefreshToken(refreshTokenRequestDTO.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequestDTO.getUsername());
        return AuthenticationResponseDTO.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequestDTO.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()).toString())
                .username(refreshTokenRequestDTO.getUsername())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public void logout(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequestDTO.getRefreshToken());
    }

    public ResponseEntity<Object> sendChangePasswordEmail() {
        User currentUser = userService.getCurrentUser();
        ResetPasswordToken token = ResetPasswordToken.builder()
                .user(currentUser)
                .uuid(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(60 * 60 * 24))
                .build();

        resetPasswordTokenRepository.save(token);
        mailService.sendMail(new NotificationEmail("Zmiana Hasła",
                currentUser.getEmail(), "Aby zmienic hasło kliknij w poniższy link:" +
                "Backend: http://localhost:8080/api/auth/resetPassword/" + token.getUuid()));
        return new ResponseEntity<>(OK);
    }

    public ResponseEntity<Object> resetPassword(String uuid, String password) {
        ResetPasswordToken token = resetPasswordTokenRepository.findByUuid(uuid).orElseThrow(() -> new NotFoundRequestException(""));

        if (token.getExpiryDate().isBefore(Instant.now()))
            throw new UnauthorizedRequestException("Ticket Expired");

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(password));
        resetPasswordTokenRepository.delete(token);
        return new ResponseEntity<>(OK);
    }
}
