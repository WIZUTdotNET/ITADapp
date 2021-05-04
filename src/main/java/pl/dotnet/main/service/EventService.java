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
import pl.dotnet.main.expections.ConnectExpection;
import pl.dotnet.main.mapper.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

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
        return eventRepository.findAll().stream()
                .map(eventMapper::eventToDto)
                .collect(Collectors.toList());
    }

    public EventDTO findById(Long id) {
        return eventMapper.eventToDto(eventRepository.findById(id).orElse(null));
    }

    public List<EventDTO> findByName(String name) {
        return eventRepository.findAll().stream()
                .map(eventMapper::eventToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseEntity<Event> add(CreateEventDTO event) {

        String username = userService.getCurrentUserName();
        Event newEvent = Event.builder().name(event.getName())
                .description(event.getDescription())
                .startDate(event.getStartTime())
                .owner(userRepository.findByUsername(username).orElseThrow(() -> new ConnectExpection("Event not found")))
                .build();

        eventRepository.save(newEvent);

        return new ResponseEntity<>(newEvent, OK);
    }

    public ResponseEntity<String> update(Event newEvent) {

        User user = userRepository.findByUsername(userService.getCurrentUserName()).orElseThrow(() -> new ConnectExpection("User not found"));
        Event oldEvent = eventRepository.findById(newEvent.getEventId()).orElseThrow(() -> new ConnectExpection("Event not found"));

        if (!userRepository.findById(oldEvent.getOwner().getUserId()).orElseThrow().equals(user))
            return new ResponseEntity<>("", FORBIDDEN);

        newEvent.setOwner(user);
        eventRepository.save(newEvent);

        return new ResponseEntity<>("Update Successful", OK);
    }

    public ResponseEntity<String> deleteById(Long id) {

        User user = userRepository.findByUsername(userService.getCurrentUserName()).orElseThrow(() -> new ConnectExpection("User not found"));
        Event newEvent = eventRepository.findById(id).orElseThrow(() -> new ConnectExpection("Event not found"));

        if (!userRepository.findById(newEvent.getOwner().getUserId()).orElseThrow().equals(user))
            return new ResponseEntity<>("", FORBIDDEN);

        eventRepository.deleteById(id);

        return new ResponseEntity<>("Deletion Successful", OK);
    }
}
