package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.User;
import pl.dotnet.main.dao.repository.EventRepository;
import pl.dotnet.main.expections.DuplitatedEntityExpection;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Iterable<Event> findAll() {
        return eventRepository.findAll();
    }

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public Iterable<Event> findByName(String name) {
        return eventRepository.findByName(name);
    }

    public Iterable<Event> findByOwner(User user) {
        return eventRepository.findByOwner(user);
    }

    public Iterable<Event> findByStartDate(Instant date) {
        return eventRepository.findByStartDate(date);
    }

    public Iterable<Event> findByEndDate(Instant date) {
        return eventRepository.findByEndDate(date);
    }

    @Transactional
    public Event add(Event event) {

        if (Objects.isNull(event.getEventId()))
            return eventRepository.save(event);
        else
            throw new DuplitatedEntityExpection("Event exists");
    }

    public Event update(Event event) {
        return eventRepository.save(event);
    }

    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() {
//        save(new Event(1L, "Przykladowa nazwa", "Przykladowy opis", Instant.now(), Instant.now(), 1L, 1L, 1L, null, null, null, null, null));
    }


}
