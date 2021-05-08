package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.Speaker;
import pl.dotnet.main.dao.repository.EventRepository;
import pl.dotnet.main.dao.repository.SpeakerRepository;
import pl.dotnet.main.dto.Speaker.CreateSpeakerDTO;
import pl.dotnet.main.dto.Speaker.SpeakerDTO;
import pl.dotnet.main.expections.ConnectException;
import pl.dotnet.main.mapper.SpeakerMapper;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Service
@AllArgsConstructor
public class SpeakerService {

    private final SpeakerRepository speakerRepository;
    private final SpeakerMapper speakerMapper;
    private final EventRepository eventRepository;
    private final UserService userService;

    public List<SpeakerDTO> findSpeakersByEventId(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        return speakerRepository.findAllByEvent(event).stream()
                .map(speakerMapper::speakerToDto)
                .collect(Collectors.toList());
    }

    public SpeakerDTO findSpeakerById(Long speakerId) {
        return speakerMapper.speakerToDto(speakerRepository.findById(speakerId).orElse(null));
    }

    public ResponseEntity<?> addSpeaker(CreateSpeakerDTO createSpeakerDTO) {
        Event event = eventRepository.findById(createSpeakerDTO.getEventId()).orElseThrow(() -> new ConnectException("Event not found"));
        if (userService.isCurrentUserNotTheOwnerOfThisEvent(event)) return new ResponseEntity<>(FORBIDDEN);

        Speaker newSpeaker = Speaker.builder()
                .name(createSpeakerDTO.getName())
                .surname(createSpeakerDTO.getSurname())
                .description(createSpeakerDTO.getDescription())
                .event(event)
                .build();

        speakerRepository.save(newSpeaker);
        return new ResponseEntity<>(OK);
    }

    public ResponseEntity<?> deleteSpeakerById(Long speakerId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ConnectException("Event not found"));
        if (userService.isCurrentUserNotTheOwnerOfThisEvent(event)) return new ResponseEntity<>(FORBIDDEN);

        Speaker speaker = speakerRepository.findById(speakerId).orElseThrow(() -> new ConnectException("Speaker not found"));

        if (speaker.getEvent().equals(event)) {
            speakerRepository.deleteById(speakerId);
            return new ResponseEntity<>(OK);
        }
        return new ResponseEntity<>(FORBIDDEN);
    }

    public ResponseEntity<?> editSpeakerById(CreateSpeakerDTO createSpeakerDTO, Long speakerId) {
        Event event = eventRepository.findById(createSpeakerDTO.getEventId()).orElseThrow(() -> new ConnectException("Event not found"));
        if (userService.isCurrentUserNotTheOwnerOfThisEvent(event)) return new ResponseEntity<>(FORBIDDEN);

        Speaker oldSpeaker = speakerRepository.findById(speakerId).orElseThrow(() -> new ConnectException("Speaker not found"));

        if (oldSpeaker.getEvent().equals(event)) {

            Speaker newSpeaker = Speaker.builder()
                    .speakerId(oldSpeaker.getSpeakerId())
                    .name(createSpeakerDTO.getName())
                    .surname(createSpeakerDTO.getSurname())
                    .description(createSpeakerDTO.getDescription())
                    .event(event)
                    .build();

            speakerRepository.save(newSpeaker);

            return new ResponseEntity<>(OK);
        }
        return new ResponseEntity<>(FORBIDDEN);
    }
}
