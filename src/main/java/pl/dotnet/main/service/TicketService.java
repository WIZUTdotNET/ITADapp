package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.dotnet.main.dao.model.Ticket;
import pl.dotnet.main.dao.repository.TicketRepository;
import pl.dotnet.main.expections.NotFoundRequestException;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public ResponseEntity<String> pay(String ticketUUId) {
        Ticket ticket = ticketRepository.findByUuid(ticketUUId).orElseThrow(() -> new NotFoundRequestException("Ticket not found"));

        ticket.setIsPayed(true);
        ticketRepository.save(ticket);
        return new ResponseEntity<>("Payment received", HttpStatus.OK);
    }
}
