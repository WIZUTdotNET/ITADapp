package pl.dotnet.main.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dotnet.main.dao.model.Lecture;
import pl.dotnet.main.dao.model.Question;
import pl.dotnet.main.dao.model.User;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByLecture(Lecture lecture);

    List<Question> findAllByUser(User user);
}
