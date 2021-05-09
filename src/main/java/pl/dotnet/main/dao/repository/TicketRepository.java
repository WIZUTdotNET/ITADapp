package pl.dotnet.main.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dotnet.main.dao.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
