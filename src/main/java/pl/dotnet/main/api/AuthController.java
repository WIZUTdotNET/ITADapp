package pl.dotnet.main.api;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.dto.AuthenticationResponseDTO;
import pl.dotnet.main.dto.LoginRequestDTO;
import pl.dotnet.main.dto.RefreshTokenRequestDTO;
import pl.dotnet.main.dto.RegisterRequestDTO;
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
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", OK);
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
}
