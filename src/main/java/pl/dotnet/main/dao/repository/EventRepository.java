package pl.dotnet.main.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.User;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByOwner(User user);

    List<Event> findByName(String name);

    List<Event> findAll();
}
