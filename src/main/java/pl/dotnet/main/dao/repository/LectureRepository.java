package pl.dotnet.main.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dotnet.main.dao.model.Lecture;
import pl.dotnet.main.dao.model.Speaker;

import java.time.Instant;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    Optional<Lecture> findByName(String name);

    Optional<Lecture> findByStartDate(Instant date);

    Optional<Lecture> findByEndDate(Instant date);

    Optional<Lecture> findBySpeakers(Speaker speaker);
}
