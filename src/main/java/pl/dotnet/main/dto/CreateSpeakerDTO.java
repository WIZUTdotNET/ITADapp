package pl.dotnet.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSpeakerDTO {

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private String description;

    @NotNull
    private Long eventId;
}
