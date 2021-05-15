package pl.dotnet.main.api;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.dto.Event.*;
import pl.dotnet.main.dto.Ticket.TicketDTO;
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
    public List<EventDTO> getCurrentUserEvents() {
        return eventService.getCurrentUserEvents();
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
    public ResponseEntity<String> registerOnEvent(@RequestParam Long eventId) {
        return eventService.registerToEvent(eventId);
    }

    @GetMapping("/getRegisteredUsers")
    public List<TicketDTO> getUsersRegisteredOnEvent(@RequestParam Long eventId) {
        return eventService.getRegisteredUsers(eventId);
    }

    @PostMapping("/MarkPresents")
    public ResponseEntity<Object> markUserAsAttendedOnEvent(@RequestParam String userUUID, Long eventId) {
        return eventService.markUserAsAttended(userUUID, eventId);
    }

    @PostMapping("/MarkPresentsAlt")
    @ApiOperation(value = "markAsAttended", notes = "Alternatywna metoda do potwierdzenia obecnosci przez uuid biletu")
    public ResponseEntity<Object> markUserAsAttendedOnEvent(@RequestParam String ticketUuid) {
        return eventService.markUserAsAttended(ticketUuid);
    }

    @GetMapping("/isCurrentUserRegistered")
    public boolean isCurrentUserRegistered(@RequestParam Long eventID){
        return eventService.isCurrentUserRegistered(eventID);
    }
}
