package pl.dotnet.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dotnet.main.model.Lecture;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
