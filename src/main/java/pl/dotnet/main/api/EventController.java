package pl.dotnet.main.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dto.Event.CreateEventDTO;
import pl.dotnet.main.dto.Event.DetailedEventDTO;
import pl.dotnet.main.dto.Event.EventDTO;
import pl.dotnet.main.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/event")
@AllArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/all")
    public List<EventDTO> getAll() {
        return eventService.findAll();
    }

    @GetMapping("/findById")
    public DetailedEventDTO getById(@RequestParam Long id) {
        return eventService.findById(id);
    }

    @GetMapping("/findByName")
    public Iterable<EventDTO> getByName(@RequestParam String name) {
        return eventService.findByName(name);
    }

    @PostMapping
    public ResponseEntity<EventDTO> addEvent(@RequestBody CreateEventDTO event) {
        return eventService.addEvent(event);
    }

    @PutMapping
    public ResponseEntity<String> updateEvent(@RequestBody Event event) {
        return eventService.update(event);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteEvent(@RequestParam Long id) {
        return eventService.deleteById(id);
    }
}
