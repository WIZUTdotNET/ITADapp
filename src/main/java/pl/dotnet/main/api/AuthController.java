package pl.dotnet.main.api;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.service.AuthService;
import pl.dotnet.main.dto.AuthenticationResponse;
import pl.dotnet.main.dto.LoginRequest;
import pl.dotnet.main.dto.RefreshTokenRequest;
import pl.dotnet.main.dto.RegisterRequest;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        if (authService.signup(registerRequest))
            return new ResponseEntity<>("User Registration Successful",
                    OK);
        else return new ResponseEntity<>("User exists",
                EXPECTATION_FAILED);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {

        authService.logout(refreshTokenRequest);

        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
    }
}
