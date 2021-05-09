package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.Ticket;
import pl.dotnet.main.dao.model.User;
import pl.dotnet.main.dao.repository.EventRepository;
import pl.dotnet.main.dao.repository.TicketRepository;
import pl.dotnet.main.dao.repository.UserRepository;
import pl.dotnet.main.dto.Event.CreateEventDTO;
import pl.dotnet.main.dto.Event.DetailedEventDTO;
import pl.dotnet.main.dto.Event.EventDTO;
import pl.dotnet.main.dto.Event.UpdateEventDTO;
import pl.dotnet.main.expections.EventFullException;
import pl.dotnet.main.expections.NotFoundRequestException;
import pl.dotnet.main.mapper.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@Service
@AllArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EventMapper eventMapper;
    private final TicketRepository ticketRepository;

    public List<EventDTO> findAll() {
        return eventRepository.findAll().stream()
                .map(eventMapper::eventToDto)
                .collect(Collectors.toList());
    }

    public DetailedEventDTO findById(Long id) {
        return eventMapper.eventToDetailedDto(eventRepository.findById(id).orElseThrow(() -> new NotFoundRequestException("Event not found")));
    }

    public List<EventDTO> findByName(String name) {
        return eventRepository.findAllByName(name).stream()
                .map(eventMapper::eventToDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<EventDTO> addEvent(CreateEventDTO event) {
        User currentUser = userService.getCurrentUser();
        Event newEvent = Event.builder()
                .name(event.getName())
                .description(event.getDescription())
                .startDate(event.getStartTime())
                .owner(currentUser)
                .build();
        eventRepository.save(newEvent);

        currentUser.addEventToOwned(newEvent);
        userRepository.save(currentUser);
        return new ResponseEntity<>(eventMapper.eventToDto(newEvent), OK);
    }

    public ResponseEntity<String> updateEvent(UpdateEventDTO eventDTO) {
        Event oldEvent = eventRepository.findById(eventDTO.getEventId()).orElseThrow(() -> new NotFoundRequestException("Event not found"));

        userService.isCurrentUserNotTheOwnerOfThisEvent(oldEvent);

        Event newEvent = Event.builder()
                .eventId(oldEvent.getEventId())
                .name(eventDTO.getName())
                .description(eventDTO.getDescription())
                .availableTickets(eventDTO.getAvailableTickets())
                .bookedTickets(eventDTO.getBookedTickets())
                .ticketPrice(eventDTO.getTicketPrice())
                .owner(oldEvent.getOwner())
                .partners(oldEvent.getPartners())
                .lectures(oldEvent.getLectures())
                .registeredUsers(oldEvent.getRegisteredUsers())
                .attendedUsers(oldEvent.getAttendedUsers())
                .build();
        eventRepository.save(newEvent);
        return new ResponseEntity<>("Update Successful", OK);
    }

    public ResponseEntity<String> deleteById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundRequestException("Event not found"));

        userService.isCurrentUserNotTheOwnerOfThisEvent(event);

        eventRepository.deleteById(id);
        return new ResponseEntity<>("Deletion Successful", OK);
    }

    public ResponseEntity<String> registerToEvent(Long eventId) {
        User currentUser = userService.getCurrentUser();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundRequestException("Event not found"));

        if (event.getAvailableTickets() <= event.getBookedTickets()) {
            throw new EventFullException("No tickets available");
        }

        event.registerUser(currentUser);
        eventRepository.save(event);

        currentUser.registerOnEvent(event);
        userRepository.save(currentUser);
        Ticket ticket = Ticket.builder()
                .event(event)
                .user(currentUser)
                .price(event.getTicketPrice())
                .isPayed(false)
                .build();

        ticketRepository.save(ticket);
        return new ResponseEntity<String>("xd", OK);
    }
}
