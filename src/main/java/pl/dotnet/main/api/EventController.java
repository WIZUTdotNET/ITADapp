package pl.dotnet.main.api;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dto.EventDTO;
import pl.dotnet.main.service.EventService;

import java.util.Optional;

@RestController
@RequestMapping("/api/event")
@AllArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/all")
    public ResponseEntity<Iterable<Event>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.findAll());
    }

    @GetMapping("/findById")
    public Optional<Event> getById(@RequestParam Long id) {
        return eventService.findById(id);
    }

    @GetMapping("/findByName")
    public Iterable<Event> getByName(@RequestParam String name) {
        return eventService.findByName(name);
    }

    @PostMapping
    public void addEvent(@RequestBody EventDTO eventDTO){
        eventService.add(eventDTO);
    }

    @PutMapping
    public Event updateEvent(@RequestBody Event event) {
        return eventService.update(event);
    }

    @DeleteMapping
    public void deleteEvent(@RequestParam Long id) {
        eventService.deleteById(id);
    }
}
