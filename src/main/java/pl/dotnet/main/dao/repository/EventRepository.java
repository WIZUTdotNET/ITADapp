package pl.dotnet.main.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.User;

import java.time.Instant;

public interface EventRepository extends JpaRepository<Event, Long> {


    Iterable<Event> findByOwner(User user);

    Iterable<Event> findByName(String name);

    Iterable<Event> findByStartDate(Instant date);

    Iterable<Event> findByEndDate(Instant date);
}
