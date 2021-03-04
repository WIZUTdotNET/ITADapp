package pl.dotnet.main.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dotnet.main.dao.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
