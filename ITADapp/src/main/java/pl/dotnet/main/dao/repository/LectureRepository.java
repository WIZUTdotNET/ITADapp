package pl.dotnet.main.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dotnet.main.dao.model.Lecture;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
