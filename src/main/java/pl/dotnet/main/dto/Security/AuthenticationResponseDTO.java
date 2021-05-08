package pl.dotnet.main.dto.Security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponseDTO {

    private String authenticationToken;
    private String refreshToken;
    private String expiresAt;
    private String username;
}

