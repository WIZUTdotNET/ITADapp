package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.User;
import pl.dotnet.main.dao.repository.EventRepository;
import pl.dotnet.main.dao.repository.UserRepository;
import pl.dotnet.main.dto.CreateEventDTO;
import pl.dotnet.main.dto.EventDTO;
import pl.dotnet.main.mapper.EventMapper;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EventMapper eventMapper;

    public List<EventDTO> findAll() {
        return eventMapper.eventToDtoList(eventRepository.findAll());
    }

    public Optional<Event> findById(Long id) {

        return eventRepository.findById(id);

    }

    public Iterable<Event> findByName(String name) {
        return eventRepository.findByName(name);
    }

    @Transactional
    public ResponseEntity<Event> add(CreateEventDTO event) {

        String username = userService.getCurrentUserName();
        Event newEvent = Event.builder().name(event.getName())
                .description(event.getDescription())
                .startDate(event.getStartTime())
                .owner(userRepository.findByUsername(username).orElseThrow())
                .build();

        eventRepository.save(newEvent);

        return new ResponseEntity<>(newEvent, OK);
    }

    public ResponseEntity<String> update(Event event) {

        User user = userRepository.findByUsername(userService.getCurrentUserName()).orElseThrow();
        Event newEvent = eventRepository.findById(event.getEventId()).orElseThrow();

        if (!userRepository.findById(newEvent.getOwner().getUserId()).orElseThrow().equals(user))
            return new ResponseEntity<>("", FORBIDDEN);

        eventRepository.save(event);

        return new ResponseEntity<>("Update Successful", OK);
    }

    public ResponseEntity<String> deleteById(Long id) {

        User user = userRepository.findByUsername(userService.getCurrentUserName()).orElseThrow();
        Event newEvent = eventRepository.findById(id).orElseThrow();

        if (!userRepository.findById(newEvent.getOwner().getUserId()).orElseThrow().equals(user))
            return new ResponseEntity<>("", FORBIDDEN);

        eventRepository.deleteById(id);

        return new ResponseEntity<>("Deletion Successful", OK);
    }
}
