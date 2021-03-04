package pl.dotnet.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dotnet.main.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
