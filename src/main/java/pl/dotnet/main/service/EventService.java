package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.NotificationEmail;
import pl.dotnet.main.dao.model.Ticket;
import pl.dotnet.main.dao.model.User;
import pl.dotnet.main.dao.repository.EventPartnerRepository;
import pl.dotnet.main.dao.repository.EventRepository;
import pl.dotnet.main.dao.repository.LectureRepository;
import pl.dotnet.main.dao.repository.TicketRepository;
import pl.dotnet.main.dto.Event.*;
import pl.dotnet.main.dto.Ticket.CreateTicketDTO;
import pl.dotnet.main.dto.Ticket.RegisterTicketDTO;
import pl.dotnet.main.expections.EventFullException;
import pl.dotnet.main.expections.NotFoundRequestException;
import pl.dotnet.main.mapper.EventMapper;
import pl.dotnet.main.mapper.TicketMapper;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@Service
@AllArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final TicketMapper ticketMapper;
    private final EventMapper eventMapper;
    private final TicketRepository ticketRepository;
    private final MailService mailService;
    private final EventPartnerRepository eventPartnerRepository;
    private final LectureRepository lectureRepository;

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

    public List<EventDTO> getCurrentUserEvent() {
        User currentUser = userService.getCurrentUser();
        return eventRepository.findByOwner(currentUser).stream()
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
                .availableTickets(1000L)
                .bookedTickets(0L)
                .ticketPrice(0D)
                .build();

        currentUser.addEventToOwned(eventRepository.save(newEvent));
        return new ResponseEntity<>(eventMapper.eventToDto(newEvent), OK);
    }

    public ResponseEntity<String> updateEvent(UpdateEventDTO eventDTO) {
        Event oldEvent = eventRepository.findById(eventDTO.getEventId()).orElseThrow(() -> new NotFoundRequestException("Event not found"));

        userService.isCurrentUserNotTheOwnerOfThisEvent(oldEvent);

        oldEvent.setName(eventDTO.getName());
        oldEvent.setDescription(eventDTO.getDescription());
        oldEvent.setStartDate(Instant.parse(eventDTO.getStartDate()));
        oldEvent.setAvailableTickets(eventDTO.getAvailableTickets());
        oldEvent.setTicketPrice(eventDTO.getTicketPrice());

        return new ResponseEntity<>("Update Successful", OK);
    }

    public ResponseEntity<String> deleteById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundRequestException("Event not found"));

        userService.isCurrentUserNotTheOwnerOfThisEvent(event);
        userService.getCurrentUser().removeEventFromOwned(event);

//        event.getPartners().forEach(eventPartnerRepository::delete);
//        event.getLectures().forEach(lectureRepository::delete);
//        event.getRegisteredUsers().forEach(ticket -> ticket.setEvent(null));
//        event.getAttendedUsers().forEach(ticket -> ticket.setEvent(null));
//        event.getOwner().removeEventFromOwned(event);

        eventRepository.deleteById(id);
        return new ResponseEntity<>("Deletion Successful", OK);
    }

    public ResponseEntity<String> registerToEvent(RegisterTicketDTO ticketDTO) {
        User currentUser = userService.getCurrentUser();
        Event event = eventRepository.findById(ticketDTO.getEventId()).orElseThrow(() -> new NotFoundRequestException("Event not found"));

        if (event.getAvailableTickets() <= event.getBookedTickets())
            throw new EventFullException("No tickets available");

        Ticket ticket = Ticket.builder()
                .event(event)
                .user(currentUser)
                .price(event.getTicketPrice())
                .isPayed(false)
                .name(ticketDTO.getName())
                .surname(ticketDTO.getSurname())
                .uuid(UUID.randomUUID().toString())
                .build();

        event.registerUser(ticket);
        currentUser.registerOnEvent(ticket);

        ticketRepository.save(ticket);

        mailService.sendMail(new NotificationEmail("Potwierdzenie rejestracji na wydzrzenie",
                currentUser.getEmail(), "Aby oplacic rezerwacje kliknij w poniższy link:" +
                "Backend: http://localhost:8080/api/ticket/ticketVerification/" + ticket.getUuid()));
        return new ResponseEntity<>(OK);
    }

    public List<CreateTicketDTO> getRegisteredUsers(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        return event.getRegisteredUsers().stream()
                .map(ticketMapper::ticketToDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<String> updateEventTickets(UpdateEventTicketsDTo eventTicketsDTo) {
        Event event = eventRepository.findById(eventTicketsDTo.getEventId()).orElseThrow();
        userService.isCurrentUserNotTheOwnerOfThisEvent(event);

        event.setAvailableTickets(eventTicketsDTo.getAvailableTickets());
        event.setTicketPrice(eventTicketsDTo.getTicketPrice());
        return new ResponseEntity<>(OK);
    }
}
