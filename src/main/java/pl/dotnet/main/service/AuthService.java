package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextException;
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
import pl.dotnet.main.dao.model.User;
import pl.dotnet.main.dao.model.VerificationToken;
import pl.dotnet.main.dao.repository.UserRepository;
import pl.dotnet.main.dao.repository.VerificationTokenRepository;
import pl.dotnet.main.dto.AuthenticationResponse;
import pl.dotnet.main.dto.LoginRequest;
import pl.dotnet.main.dto.RefreshTokenRequest;
import pl.dotnet.main.dto.RegisterRequest;
import pl.dotnet.main.expections.ConnectExpection;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserValidator userValidator;

    @Transactional
    public void signup(RegisterRequest registerRequest) {

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .created(Instant.now())
                .isActive(false)
                .build();

        if (userValidator.validateUser(user)) {
            userRepository.save(user);

            String token = generateVerifivationToken(user);

            mailService.sendMail(new NotificationEmail("Potwierdzenie rejestracji",
                    user.getEmail(), "Aby aktywować konto kliknij w poniższy link: " +
                    "http://localhost:8080/api/auth/accountVerification/" + token));
            return;
        }
        throw new ApplicationContextException("Username or email taken");
    }

    private String generateVerifivationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new ConnectExpection("Invalid Token")));
        verificationTokenRepository.deleteById(verificationToken.get().getId());
    }

    @Transactional
    void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ConnectExpection("User not found with name - " + username));
        user.setIsActive(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public void logout(RefreshTokenRequest refreshTokenRequest) {

        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
    }
}
