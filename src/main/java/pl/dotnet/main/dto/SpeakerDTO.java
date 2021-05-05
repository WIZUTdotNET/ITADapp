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
public class SpeakerDTO {

    @NotNull
    private Long speakerId;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private String description;

    @NotNull
    private Long eventId;
}
