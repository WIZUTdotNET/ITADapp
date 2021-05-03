package pl.dotnet.main.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.Speaker;

import java.util.List;

public interface SpeakerRepository extends JpaRepository<Speaker, Long> {

    List<Speaker> findAllByEvent(Event event);
}
