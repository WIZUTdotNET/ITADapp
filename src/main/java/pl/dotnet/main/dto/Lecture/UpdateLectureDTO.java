package pl.dotnet.main.dto.Lecture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLectureDTO {

    @NotNull
    private Long lectureId;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Instant startDate;
}
