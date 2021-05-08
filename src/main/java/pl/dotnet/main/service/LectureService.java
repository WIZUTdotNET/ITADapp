package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.Lecture;
import pl.dotnet.main.dao.model.Speaker;
import pl.dotnet.main.dao.repository.EventRepository;
import pl.dotnet.main.dao.repository.LectureRepository;
import pl.dotnet.main.dao.repository.SpeakerRepository;
import pl.dotnet.main.dto.CreateLectureDTO;
import pl.dotnet.main.dto.LectureDTO;
import pl.dotnet.main.expections.ConnectException;
import pl.dotnet.main.mapper.LectureMapper;

import java.util.List;
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

    public LectureDTO findLectureById(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow();
        return lectureMapper.lectureToDTO(lecture);
    }

    public List<LectureDTO> findLecturesByEventId(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        return lectureRepository.findAllByEvent(event).stream()
                .map(lectureMapper::lectureToDTO)
                .collect(Collectors.toList());
    }

    public ResponseEntity<String> addLecture(CreateLectureDTO lectureDTO) {
        Event event = eventRepository.findById(lectureDTO.getEventId()).orElseThrow(() -> new ConnectException("Event not found"));
        if (userService.isCurrentUserNotTheOwnerOfThisEvent(event)) return new ResponseEntity<>(FORBIDDEN);

        Lecture newLecture = Lecture.builder()
                .name(lectureDTO.getName())
                .description(lectureDTO.getDescription())
                .startDate(lectureDTO.getStartDate())
                .event(event)
                .build();

        lectureRepository.save(newLecture);
        return new ResponseEntity<>(OK);
    }

    public ResponseEntity<String> editLectureById(CreateLectureDTO lectureDTO, Long lectureId) {
        Event event = eventRepository.findById(lectureDTO.getEventId()).orElseThrow(() -> new ConnectException("Event not found"));
        if (userService.isCurrentUserNotTheOwnerOfThisEvent(event)) return new ResponseEntity<>(FORBIDDEN);

        Lecture oldLecture = lectureRepository.findById(lectureId).orElseThrow();

        if (oldLecture.getEvent().equals(event)) {
            Lecture newLecture = Lecture.builder()
                    .lectureId(oldLecture.getLectureId())
                    .name(lectureDTO.getName())
                    .description(lectureDTO.getDescription())
                    .startDate(lectureDTO.getStartDate())
                    .availableSeats(oldLecture.getAvailableSeats())
                    .takenSeats(oldLecture.getTakenSeats())
                    .event(event)
                    .speakers(oldLecture.getSpeakers())
                    .registeredUsers(oldLecture.getRegisteredUsers())
                    .attendedUsers(oldLecture.getAttendedUsers())
                    .build();
            lectureRepository.save(newLecture);
            return new ResponseEntity<>(OK);

        }
        return new ResponseEntity<>(FORBIDDEN);
    }

    public ResponseEntity<String> deleteLecture(Long lectureId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ConnectException("Event not found"));
        if (userService.isCurrentUserNotTheOwnerOfThisEvent(event)) return new ResponseEntity<>(FORBIDDEN);

        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new ConnectException("Speaker not found"));

        if (lecture.getEvent().equals(event)) {
            lectureRepository.deleteById(lectureId);
            return new ResponseEntity<>(OK);
        }
        return new ResponseEntity<>(FORBIDDEN);
    }

    public ResponseEntity<String> addSpeakerToLecture(Long speakerId, Long lectureId) {

        Speaker speaker = speakerRepository.findById(speakerId).orElseThrow();

        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow();

        if (!lecture.getEvent().equals(speaker.getEvent()))
            return new ResponseEntity<>(FORBIDDEN);

        if (userService.isCurrentUserNotTheOwnerOfThisEvent(lecture.getEvent()))
            return new ResponseEntity<>(FORBIDDEN);

        List<Speaker> speakerList = lecture.getSpeakers();
        speakerList.add(speaker);
        lecture.setSpeakers(speakerList);

        lectureRepository.save(lecture);

        return new ResponseEntity<>(OK);
    }

    public ResponseEntity<String> removeSpeakerFromLecture(Long speakerId, Long lectureId) {
        Speaker speaker = speakerRepository.findById(speakerId).orElseThrow();

        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow();

        if (!lecture.getEvent().equals(speaker.getEvent()))
            return new ResponseEntity<>(FORBIDDEN);

        if (userService.isCurrentUserNotTheOwnerOfThisEvent(lecture.getEvent()))
            return new ResponseEntity<>(FORBIDDEN);

        List<Speaker> speakerList = lecture.getSpeakers();
        speakerList.remove(speaker);
        lecture.setSpeakers(speakerList);

        lectureRepository.save(lecture);

        return new ResponseEntity<>(OK);
    }
}
