package pl.dotnet.main.dto.Question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AskQuestionDTO {

    @NotNull
    private String question;

    @NotNull
    private Long lectureId;
}
