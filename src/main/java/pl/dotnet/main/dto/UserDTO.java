package pl.dotnet.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    @NotNull
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String surname;
}
