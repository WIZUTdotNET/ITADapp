package pl.dotnet.main.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.dto.Speaker.CreateSpeakerDTO;
import pl.dotnet.main.dto.Speaker.SpeakerDTO;
import pl.dotnet.main.dto.Speaker.UpdateSpeakerDTO;
import pl.dotnet.main.service.SpeakerService;

import java.util.List;

@RestController
@RequestMapping("/api/speaker")
@AllArgsConstructor
public class SpeakerController {

    private final SpeakerService speakerService;

    @PostMapping
    public ResponseEntity<SpeakerDTO> addSpeaker(@RequestBody CreateSpeakerDTO speakerDTO) {
        return speakerService.addSpeaker(speakerDTO);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteSpeaker(@RequestParam Long speakerId, Long eventId) {
        return speakerService.deleteSpeakerById(speakerId, eventId);
    }

    @PutMapping
    public ResponseEntity<String> editSpeaker(@RequestBody UpdateSpeakerDTO speakerDTO) {
        return speakerService.editSpeakerById(speakerDTO);
    }

    @GetMapping("/getSpeakersFormEvent")
    public List<SpeakerDTO> getSpeakersFormEvent(@RequestParam Long eventId) {
        return speakerService.findSpeakersByEventId(eventId);
    }

    @GetMapping("/getSpeaker")
    public SpeakerDTO getSpeakerById(@RequestParam Long speakerId) {
        return speakerService.findSpeakerById(speakerId);
    }
}
