package pl.dotnet.main.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dotnet.main.service.TicketService;

@RestController
@RequestMapping("/api/ticket")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/ticketVerification/{ticketUUId}")
    public ResponseEntity<String> payTicket(@PathVariable String ticketUUId) {
        return ticketService.pay(ticketUUId);
    }
}
