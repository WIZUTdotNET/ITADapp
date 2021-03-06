package pl.dotnet.main.dto.Lecture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dotnet.main.dto.Question.QuestionDTO;
import pl.dotnet.main.dto.Speaker.SpeakerDTO;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private Long eventId;

    @NotNull
    private List<SpeakerDTO> speakers;

    @NotNull
    private List<Long> speakersIds;

    @NotNull
    private List<QuestionDTO> questions;
}
