package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.Speaker;
import pl.dotnet.main.dao.model.User;
import pl.dotnet.main.dao.repository.EventRepository;
import pl.dotnet.main.dao.repository.SpeakerRepository;
import pl.dotnet.main.dao.repository.UserRepository;
import pl.dotnet.main.dto.CreateSpeakerDTO;
import pl.dotnet.main.dto.SpeakerDTO;
import pl.dotnet.main.mapper.SpeakerMapper;

import java.util.List;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Service
@AllArgsConstructor
public class SpeakerService {
    private final SpeakerRepository speakerRepository;
    private final EventRepository eventRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final SpeakerMapper speakerMapper;

    public ResponseEntity<?> addSpeaker(CreateSpeakerDTO createSpeakerDTO) {

        Event event = eventRepository.findById(createSpeakerDTO.getEventId()).orElseThrow();
        User currentUser = userRepository.findByUsername(userService.getCurrentUserName()).orElseThrow();
        if (!userRepository.findById(event.getOwner().getUserId()).orElseThrow().equals(currentUser))
            return new ResponseEntity<>(FORBIDDEN);

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

        Event event = eventRepository.findById(eventId).orElseThrow();
        User currentUser = userRepository.findByUsername(userService.getCurrentUserName()).orElseThrow();
        if (!userRepository.findById(event.getOwner().getUserId()).orElseThrow().equals(currentUser))
            return new ResponseEntity<>(FORBIDDEN);

        Speaker speaker = speakerRepository.findById(speakerId).orElseThrow();

        if (speaker.getEvent().equals(event)) {
            speakerRepository.deleteById(speakerId);
            return new ResponseEntity<>(OK);
        }
        return new ResponseEntity<>(FORBIDDEN);
    }

    public ResponseEntity<?> editSpeakerById(CreateSpeakerDTO createSpeakerDTO, Long speakerId) {

        Event event = eventRepository.findById(createSpeakerDTO.getEventId()).orElseThrow();
        User currentUser = userRepository.findByUsername(userService.getCurrentUserName()).orElseThrow();
        if (!userRepository.findById(event.getOwner().getUserId()).orElseThrow().equals(currentUser))
            return new ResponseEntity<>(FORBIDDEN);

        Speaker oldSpeaker = speakerRepository.findById(speakerId).orElseThrow();

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

    public List<SpeakerDTO> findSpeakersByEventId(Long eventId) {

        Event event = eventRepository.findById(eventId).orElseThrow();
        return speakerMapper.speakerToDto(speakerRepository.findAllByEvent(event));
    }

    public SpeakerDTO findSpeakerById(Long speakerId) {

        return speakerMapper.speakerToDto(speakerRepository.findById(speakerId).orElse(null));
    }

    //todo
    // edycja,
    // wszyscy dla eventu,
    // speaker po id,
    // asd
}
