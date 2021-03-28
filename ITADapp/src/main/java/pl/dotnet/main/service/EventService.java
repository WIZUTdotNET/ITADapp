package pl.dotnet.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.User;
import pl.dotnet.main.dao.repository.EventRepository;

import java.time.Instant;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;


    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Iterable<Event> findAll() {

        return eventRepository.findAll();
    }

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public Optional<Event> findByName(String name) {
        return eventRepository.findByName(name);
    }

    public Optional<Event> findByOwner(User user) {
        return eventRepository.findByOwner(user);
    }

    public Optional<Event> findByStartDate(Instant date) {
        return eventRepository.findByStartDate(date);
    }

    public Optional<Event> findByEndDate(Instant date) {
        return eventRepository.findByEndDate(date);
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() {
        save(new Event(1L, "name", "Desc", Instant.now(), Instant.now(), 1L, 1L, 1L, null, null, null, null, null));
    }
}
