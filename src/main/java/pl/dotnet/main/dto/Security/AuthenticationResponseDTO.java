package pl.dotnet.main.dto.Security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponseDTO {

    @NotNull
    private String authenticationToken;

    @NotNull
    private String refreshToken;

    @NotNull
    private String expiresAt;

    @NotNull
    private String username;

    @NotNull
    private Long userId;
}

