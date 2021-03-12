package pl.dotnet.main.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.User;

import java.time.Instant;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {


    Optional<Event> findByOwner(User user);

    Optional<Event> findByName(String name);

    Optional<Event> findByStartDate(Instant date);

    Optional<Event> findByEndDate(Instant date);
}
