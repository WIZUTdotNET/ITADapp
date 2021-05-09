package pl.dotnet.main.dto.Speaker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSpeakerDTO {

    @NotNull
    private Long speakerId;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private String description;
}
