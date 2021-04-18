package pl.dotnet.main.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.service.EventService;

import java.util.Optional;

@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/all")
    public Iterable<Event> getAll() {
        return eventService.findAll();
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
    public Event addEvent(@RequestBody Event event) {
        return eventService.add(event);
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
