package pl.dotnet.main.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.dto.Speaker.CreateSpeakerDTO;
import pl.dotnet.main.dto.Speaker.SpeakerDTO;
import pl.dotnet.main.service.SpeakerService;

import java.util.List;

@RestController
@RequestMapping("/api/speaker")
@AllArgsConstructor
public class SpeakerController {

    private final SpeakerService speakerService;

    @PostMapping
    public ResponseEntity<?> addSpeaker(@RequestBody CreateSpeakerDTO createSpeakerDTO) {
        return speakerService.addSpeaker(createSpeakerDTO);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteSpeaker(@RequestParam Long speakerId, Long eventId) {
        return speakerService.deleteSpeakerById(speakerId, eventId);
    }

    @PutMapping
    public ResponseEntity<?> editSpeaker(@RequestBody CreateSpeakerDTO createSpeakerDTO, @RequestParam Long speakerId) {
        return speakerService.editSpeakerById(createSpeakerDTO, speakerId);
    }

    @GetMapping("/getSpeakersFormEvent")
    public List<SpeakerDTO> getSpeakersFormEvent(@RequestParam Long eventId) {
        return speakerService.findSpeakersByEventId(eventId);
    }

    @GetMapping("/getSpeaker")
    public SpeakerDTO getSpeaker(@RequestParam Long speakerId) {
        return speakerService.findSpeakerById(speakerId);
    }
}
