package pl.dotnet.main.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.dto.Question.AskQuestionDTO;
import pl.dotnet.main.dto.Question.QuestionDTO;
import pl.dotnet.main.service.QuestionService;

import java.util.List;

@RestController
@RequestMapping("/api/question")
@AllArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<Object> askQuestion(@RequestBody AskQuestionDTO questionDTO) {
        return questionService.askQuestion(questionDTO);
    }

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> gqtQuestionsFromLecture(@RequestParam Long lectureId) {
        return questionService.getQuestionsFromLecture(lectureId);
    }
}
