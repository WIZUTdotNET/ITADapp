package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dotnet.main.dao.model.*;
import pl.dotnet.main.dao.repository.*;
import pl.dotnet.main.dto.Lecture.CreateLectureDTO;
import pl.dotnet.main.dto.Lecture.LectureDTO;
import pl.dotnet.main.dto.Lecture.UpdateLectureDTO;
import pl.dotnet.main.expections.NotFoundRequestException;
import pl.dotnet.main.expections.NotPayedException;
import pl.dotnet.main.mapper.LectureMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@AllArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final LectureMapper lectureMapper;
    private final UserService userService;
    private final EventRepository eventRepository;
    private final SpeakerRepository speakerRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public LectureDTO findLectureById(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new NotFoundRequestException("Lecture not found"));
        return lectureMapper.lectureToDTO(lecture);
    }

    public List<LectureDTO> findLecturesByEventId(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundRequestException("Event not found"));
        return lectureRepository.findAllByEvent(event).stream()
                .map(lectureMapper::lectureToDTO)
                .collect(Collectors.toList());
    }

    public ResponseEntity<LectureDTO> addLecture(CreateLectureDTO lectureDTO) {
        Event event = eventRepository.findById(lectureDTO.getEventId()).orElseThrow(() -> new NotFoundRequestException("Event not found"));
        userService.isCurrentUserNotTheOwnerOfThisEvent(event);

        Lecture newLecture = Lecture.builder()
                .name(lectureDTO.getName())
                .description(lectureDTO.getDescription())
                .startDate(lectureDTO.getStartDate())
                .event(event)
                .build();

        event.addLectureToEvent(newLecture);

        return new ResponseEntity<>(lectureMapper.lectureToDTO(lectureRepository.save(newLecture)), OK);
    }

    public ResponseEntity<String> editLectureById(UpdateLectureDTO lectureDTO) {

        Lecture oldLecture = lectureRepository.findById(lectureDTO.getLectureId()).orElseThrow(() -> new NotFoundRequestException("Lecture not found"));

        userService.isCurrentUserNotTheOwnerOfThisEvent(oldLecture.getEvent());

        oldLecture.setName(lectureDTO.getName());
        oldLecture.setDescription(lectureDTO.getDescription());
        oldLecture.setStartDate(lectureDTO.getStartDate());
        return new ResponseEntity<>(OK);
    }

    public ResponseEntity<String> deleteLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new NotFoundRequestException("Lecture not found"));

        userService.isCurrentUserNotTheOwnerOfThisEvent(lecture.getEvent());

        lecture.getEvent().removeLectureFromEvent(lecture);
        lectureRepository.deleteById(lectureId);
        return new ResponseEntity<>(OK);
    }

    public ResponseEntity<String> addSpeakerToLecture(Long speakerId, Long lectureId) {
        Speaker speaker = speakerRepository.findById(speakerId).orElseThrow(() -> new NotFoundRequestException("Speaker not found"));
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new NotFoundRequestException("Lecture not found"));

        if (!lecture.getEvent().equals(speaker.getEvent()))
            return new ResponseEntity<>(FORBIDDEN);

        userService.isCurrentUserNotTheOwnerOfThisEvent(lecture.getEvent());

        lecture.addSpeakerToLecture(speaker);
        speaker.addLecture(lecture);
        return new ResponseEntity<>(OK);
    }

    public ResponseEntity<String> removeSpeakerFromLecture(Long speakerId, Long lectureId) {
        Speaker speaker = speakerRepository.findById(speakerId).orElseThrow(() -> new NotFoundRequestException("Speaker not found"));
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new NotFoundRequestException("Lecture not found"));

        if (!lecture.getEvent().equals(speaker.getEvent()))
            return new ResponseEntity<>(FORBIDDEN);

        userService.isCurrentUserNotTheOwnerOfThisEvent(lecture.getEvent());

        lecture.removeSpeakerFromLecture(speaker);
        speaker.removeLecture(lecture);
        return new ResponseEntity<>(OK);
    }

    public ResponseEntity<Object> markUserAsAttended(UUID userUUID, Long lectureId) {

        User user = userRepository.findByUserUUID(userUUID).orElseThrow(() -> new NotFoundRequestException("User not found"));
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new NotFoundRequestException("Lecture not found"));
        Event event = lecture.getEvent();

        userService.isCurrentUserNotTheOwnerOfThisEvent(event);

        Ticket ticket = ticketRepository.findByUserAndEvent(user, event).orElseThrow(() -> new NotFoundRequestException("Ticket not found"));

        if (!ticket.getIsPayed())
            throw new NotPayedException("Ticket is not payed");

        if (lecture.getAttendedUsers().contains(ticket))
            return new ResponseEntity<>("User attended", OK);

        lecture.markTicketAsAttended(ticket);
        return new ResponseEntity<>(OK);
    }
}
