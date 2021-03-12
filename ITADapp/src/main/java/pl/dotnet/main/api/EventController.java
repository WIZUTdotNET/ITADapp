package pl.dotnet.main.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.manager.EventManager;

import java.util.Optional;

@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventManager eventManager;

    @Autowired
    public EventController(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @GetMapping("/all")
    public Iterable<Event> getAll() {
        return eventManager.findAll();
    }

    @GetMapping("/fingById")
    public Optional<Event> getById(@RequestParam Long id) {
        return eventManager.findById(id);
    }

    @GetMapping
    public Optional<Event> getByName(@RequestParam String name) {
        return eventManager.findByName(name);
    }

    @PostMapping
    public Event addEvent(@RequestBody Event event) {
        return eventManager.save(event);
    }

    @PutMapping
    public Event updateEvent(@RequestBody Event event) {
        return eventManager.save(event);
    }

    @DeleteMapping
    public void deleteEvent(@RequestParam Long id) {
        eventManager.deleteById(id);
    }
}
