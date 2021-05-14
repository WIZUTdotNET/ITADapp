package pl.dotnet.main.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.Ticket;
import pl.dotnet.main.dao.model.User;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    public Optional<Ticket> findByUuid(String uuid);

    public Optional<Ticket> findByUserAndEvent(User user, Event event);
}
