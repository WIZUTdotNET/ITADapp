package pl.dotnet.main.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.dto.Event.*;
import pl.dotnet.main.dto.Ticket.CreateTicketDTO;
import pl.dotnet.main.dto.Ticket.RegisterTicketDTO;
import pl.dotnet.main.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/event")
@AllArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/all")
    public List<EventDTO> getAllEvents() {
        return eventService.findAll();
    }

    @GetMapping("/findById")
    public DetailedEventDTO getEventById(@RequestParam Long id) {
        return eventService.findById(id);
    }

    @GetMapping("/findByName")
    public Iterable<EventDTO> getEventByName(@RequestParam String name) {
        return eventService.findByName(name);
    }

    @GetMapping("/currentUser")
    public List<EventDTO> getCurrentUserEvent() {
        return eventService.getCurrentUserEvent();
    }

    @PostMapping
    public ResponseEntity<EventDTO> addEvent(@RequestBody CreateEventDTO event) {
        return eventService.addEvent(event);
    }

    @PutMapping
    public ResponseEntity<String> updateEvent(@RequestBody UpdateEventDTO event) {
        return eventService.updateEvent(event);
    }

    @PutMapping("/updateTickets")
    public ResponseEntity<String> updateEventTickets(@RequestBody UpdateEventTicketsDTo eventTicketsDTo) {
        return eventService.updateEventTickets(eventTicketsDTo);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteEvent(@RequestParam Long id) {
        return eventService.deleteById(id);
    }

    @PostMapping("/registerOnEvent")
    public ResponseEntity<String> registerOnEvent(@RequestBody RegisterTicketDTO ticketDTO) {
        return eventService.registerToEvent(ticketDTO);
    }

    @GetMapping("/getRegisteredUsers")
    public List<CreateTicketDTO> getRegisteredUsers(@RequestParam Long eventId) {
        return eventService.getRegisteredUsers(eventId);
    }
}
