package pl.dotnet.main.dto.Security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    private String email;
    private String username;
    private String name;
    private String surname;
    private String password;
}