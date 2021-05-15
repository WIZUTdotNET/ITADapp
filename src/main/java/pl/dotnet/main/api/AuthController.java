package pl.dotnet.main.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.dto.Security.AuthenticationResponseDTO;
import pl.dotnet.main.dto.Security.LoginRequestDTO;
import pl.dotnet.main.dto.Security.RefreshTokenRequestDTO;
import pl.dotnet.main.dto.Security.RegisterRequestDTO;
import pl.dotnet.main.service.AuthService;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return authService.signup(registerRequestDTO);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        return authService.verifyAccount(token);
    }

    @PostMapping("/login")
    public AuthenticationResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return authService.login(loginRequestDTO);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponseDTO refreshTokens(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return authService.refreshToken(refreshTokenRequestDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        authService.logout(refreshTokenRequestDTO);
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Object> sendEmailToChangePassword() {
        return authService.sendChangePasswordEmail();
    }

    @GetMapping("/resetPassword/{token}")
    public ResponseEntity<Object> changePassword(@PathVariable String token, @RequestBody String password) {
        return authService.resetPassword(token, password);
    }
}
