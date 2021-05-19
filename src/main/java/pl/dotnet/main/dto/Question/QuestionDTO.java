package pl.dotnet.main.dto.Question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
    private Long userId;
    private String name;
    private String surname;
    private Long lectureId;
    private String question;
}
