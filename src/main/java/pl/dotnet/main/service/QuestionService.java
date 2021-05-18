package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.dotnet.main.dao.model.Lecture;
import pl.dotnet.main.dao.model.Question;
import pl.dotnet.main.dao.model.User;
import pl.dotnet.main.dao.repository.LectureRepository;
import pl.dotnet.main.dao.repository.QuestionRepository;
import pl.dotnet.main.dto.Question.AskQuestionDTO;
import pl.dotnet.main.dto.Question.QuestionDTO;
import pl.dotnet.main.expections.UnauthorizedRequestException;
import pl.dotnet.main.mapper.QuestionMapper;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@Service
@AllArgsConstructor
public class QuestionService {

    private final UserService userService;
    private final LectureRepository lectureRepository;
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    public ResponseEntity<Object> askQuestion(AskQuestionDTO questionDTO) {
        User currentUser = userService.getCurrentUser();
        Lecture lecture = lectureRepository.findById(questionDTO.getLectureId()).orElseThrow();

        if (lecture.getAttendedUsers().stream().noneMatch(ticket -> ticket.getUser().equals(currentUser))) {
            throw new UnauthorizedRequestException("User is not present at this event");
        }

        Question question = Question.builder()
                .user(currentUser)
                .question(questionDTO.getQuestion())
                .lecture(lecture)
                .build();

        questionRepository.save(question);
        return new ResponseEntity<>(OK);
    }

    public ResponseEntity<List<QuestionDTO>> getQuestionsFromLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow();
        List<Question> questions = questionRepository.findAllByLecture(lecture);

        return new ResponseEntity<>(questions.stream()
                .map(questionMapper::questionToDTO)
                .collect(Collectors.toList()), OK);
    }
}
