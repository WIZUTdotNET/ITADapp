package pl.dotnet.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureDTO {

    @NotNull
    private Long lectureId;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String startDate;

    @NotNull
    private Long availableSeats;

    @NotNull
    private Long takenSeats;

    @NotNull
    private Long eventId;

    @NotNull
    private List<SpeakerDTO> speakers;
}
