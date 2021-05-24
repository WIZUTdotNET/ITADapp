package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dotnet.main.dao.model.*;
import pl.dotnet.main.dao.repository.EventRepository;
import pl.dotnet.main.dao.repository.TicketRepository;
import pl.dotnet.main.dao.repository.UserRepository;
import pl.dotnet.main.dto.Event.*;
import pl.dotnet.main.dto.Ticket.TicketDTO;
import pl.dotnet.main.expections.EventFullException;
import pl.dotnet.main.expections.NotFoundRequestException;
import pl.dotnet.main.expections.NotPayedException;
import pl.dotnet.main.mapper.EventMapper;
import pl.dotnet.main.mapper.TicketMapper;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final TicketMapper ticketMapper;
    private final EventMapper eventMapper;
    private final TicketRepository ticketRepository;
    private final MailService mailService;
    private final UserRepository userRepository;

    public List<EventDTO> findAll() {
        return eventRepository.findAll().stream()
                .map(eventMapper::eventToDto)
                .collect(Collectors.toList());
    }

    public DetailedEventDTO findById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundRequestException("Event not found"));
        DetailedEventDTO detailedEventDTO = eventMapper.eventToDetailedDto(event);
        detailedEventDTO.setCurrentUserRegistered(isCurrentUserRegistered(eventId));

        return detailedEventDTO;
    }

    public List<EventDTO> findByName(String name) {
        return eventRepository.findAllByName(name).stream()
                .map(eventMapper::eventToDto)
                .collect(Collectors.toList());
    }

    public List<EventDTO> getCurrentUserEvents() {
        User currentUser = userService.getCurrentUser();
        return eventRepository.findByOwner(currentUser).stream()
                .map(eventMapper::eventToDto)
                .collect(Collectors.toList());
    }

    public List<TicketDTO> getRegisteredUsers(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        return event.getRegisteredUsers().stream()
                .map(ticketMapper::ticketToDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<EventDTO> addEvent(CreateEventDTO event) {
        User currentUser = userService.getCurrentUser();
        Event newEvent = Event.builder()
                .name(event.getName())
                .description(event.getDescription())
                .startDate(event.getStartTime())
                .availableTickets(event.getAvailableTickets())
                .ticketPrice(event.getTicketPrice())
                .owner(currentUser)
                .bookedTickets(0L)
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

        eventRepository.deleteById(id);
        return new ResponseEntity<>("Deletion Successful", OK);
    }

    public ResponseEntity<String> registerToEvent(Long eventId) {
        User currentUser = userService.getCurrentUser();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundRequestException("Event not found"));

        if (event.getAvailableTickets() <= event.getBookedTickets())
            throw new EventFullException("No tickets available");

        if (event.getRegisteredUsers().stream().anyMatch(ticket -> ticket.getUser().equals(currentUser)))
            throw new EventFullException("User already registered");

        Ticket ticket = Ticket.builder()
                .event(event)
                .user(currentUser)
                .price(event.getTicketPrice())
                .isPayed(false)
                .name(currentUser.getName())
                .surname(currentUser.getSurname())
                .uuid(UUID.randomUUID().toString())
                .build();

        event.registerTicket(ticket);
        currentUser.registerOnEvent(ticket);

        ticketRepository.save(ticket);

        mailService.sendMail(new NotificationEmail("Potwierdzenie rejestracji na wydzrzenie",
                currentUser.getEmail(), "Aby oplacic rezerwacje kliknij w poniższy link:" +
                "Backend: http://localhost:8080/api/ticket/ticketVerification/" + ticket.getUuid()));
        return new ResponseEntity<>(OK);
    }

    public ResponseEntity<String> updateEventTickets(UpdateEventTicketsDTo eventTicketsDTo) {
        Event event = eventRepository.findById(eventTicketsDTo.getEventId()).orElseThrow();
        userService.isCurrentUserNotTheOwnerOfThisEvent(event);

        event.setAvailableTickets(eventTicketsDTo.getAvailableTickets());
        event.setTicketPrice(eventTicketsDTo.getTicketPrice());
        return new ResponseEntity<>(OK);
    }

    public ResponseEntity<Object> markUserAsAttended(UUID userUUID, Long eventId) {
        System.out.println(userUUID);
        User user = userRepository.findByUserUUID(userUUID).orElseThrow(() -> new NotFoundRequestException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundRequestException("Event not found"));

        userService.isCurrentUserNotTheOwnerOfThisEvent(event);

        Ticket ticket = ticketRepository.findByUserAndEvent(user, event).orElseThrow(() -> new NotFoundRequestException("Ticket not found"));

        if (!ticket.getIsPayed())
            throw new NotPayedException("Ticket is not payed");

        if (event.getAttendedUsers().contains(ticket))
            return new ResponseEntity<>("User attended", OK);

        user.attendEvent(ticket);
        event.markTicketAsAttended(ticket);

        return new ResponseEntity<>(OK);
    }

    public ResponseEntity<Object> markUserAsAttended(String ticketUuid) {
        Ticket ticket = ticketRepository.findByUuid(ticketUuid).orElseThrow(() -> new NotFoundRequestException("Ticket not found"));
        userService.isCurrentUserNotTheOwnerOfThisEvent(ticket.getEvent());

        if (!ticket.getIsPayed())
            throw new NotPayedException("Ticket is not payed");

        if (ticket.getEvent().getAttendedUsers().contains(ticket))
            return new ResponseEntity<>("User attended", OK);

        ticket.getUser().attendEvent(ticket);
        ticket.getEvent().markTicketAsAttended(ticket);

        return new ResponseEntity<>(OK);
    }


    public boolean isCurrentUserRegistered(Long eventId) {
        User user = userService.getCurrentUserOrNull();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundRequestException("Event not found"));
        Set<Ticket> tickets = event.getRegisteredUsers().stream()
                .filter(ticket -> ticket.getUser().equals(user))
                .collect(Collectors.toSet());
        return !tickets.isEmpty();
    }

    public boolean getPresence(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundRequestException("Event not found"));
        userService.isCurrentUserNotTheOwnerOfThisEvent(event);

        List<Ticket> registeredUsers = event.getRegisteredUsers();
        List<Ticket> attendedUsers = event.getAttendedUsers();

        Map<String, Boolean> eventPresence = registeredUsers.stream()
                .collect(Collectors.toMap(Ticket::getUuid, attendedUsers::contains, (a, b) -> b));

        log.info(eventPresence.toString());

        Map<Long, Map<String, Boolean>> lecturePresence = event.getLectures().stream()
                .collect(Collectors.toMap(Lecture::getLectureId, lecture -> registeredUsers.stream()
                        .collect(Collectors.toMap(Ticket::getUuid, ticket -> lecture.getAttendedUsers().contains(ticket), (a, b) -> b)), (a1, b1) -> b1));

        log.info(lecturePresence.toString());

//        Workbook workbook = new XSSFWorkbook();
//
//        Sheet sheet = workbook.createSheet("getPresence");
//        sheet.setColumnWidth(0, 6000);
//        sheet.setColumnWidth(1, 4000);
//
//        Row header = sheet.createRow(0);
//
//        CellStyle headerStyle = workbook.createCellStyle();
//        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
//        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
//        font.setFontName("Arial");
//        font.setFontHeightInPoints((short) 16);
//        font.setBold(true);
//        headerStyle.setFont(font);
//
//        Cell headerCell = header.createCell(0);
//        headerCell.setCellValue("Name");
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(1);
//        headerCell.setCellValue("Age");
//        headerCell.setCellStyle(headerStyle);
//
//        CellStyle style = workbook.createCellStyle();
//        style.setWrapText(true);
//
//        Row row = sheet.createRow(2);
//        Cell cell = row.createCell(0);
//        cell.setCellValue("John Smith");
//        cell.setCellStyle(style);
//
//        cell = row.createCell(1);
//        cell.setCellValue(20);
//        cell.setCellStyle(style);
//
//        File currDir = new File(".");
//        String path = currDir.getAbsolutePath();
//        String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";
//
//        try{
//            FileOutputStream outputStream = new FileOutputStream(fileLocation);
//            workbook.write(outputStream);
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return true;
    }
}
