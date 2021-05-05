package pl.dotnet.main.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.dto.CreateLectureDTO;
import pl.dotnet.main.dto.LectureDTO;
import pl.dotnet.main.service.LectureService;

import java.util.List;

@RestController
@RequestMapping("/api/lecture")
@AllArgsConstructor
public class LectureController {
    private final LectureService lectureService;

    @PostMapping
    public ResponseEntity<String> addLecture(@RequestBody CreateLectureDTO lectureDTO) {
        return lectureService.addLecture(lectureDTO);
    }

    @PostMapping("/addSpeaker")
    public ResponseEntity<String> addSpeakerToLecture(@RequestParam Long speakerId, Long lectureId) {
        return lectureService.addSpeakerToLecture(speakerId, lectureId);
    }

    @DeleteMapping("/removeSpeaker")
    public ResponseEntity<String> removeSpeakerToLecture(@RequestParam Long speakerId, Long lectureId) {
        return lectureService.removeSpeakerFromLecture(speakerId, lectureId);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteLecture(@RequestParam Long lectureId, Long eventId) {
        return lectureService.deleteLecture(lectureId, eventId);
    }

    @PutMapping
    public ResponseEntity<String> editSpeaker(@RequestBody CreateLectureDTO lectureDTO, @RequestParam Long lectureId) {
        return lectureService.editLectureById(lectureDTO, lectureId);
    }

    @GetMapping("/getLectureFromEvent")
    public List<LectureDTO> getLectureFromEvent(@RequestParam Long eventId) {
        return lectureService.findLecturesByEventId(eventId);
    }

    @GetMapping("/getLecture")
    public LectureDTO getLecture(@RequestParam Long lectureId) {
        return lectureService.findLectureById(lectureId);
    }

}
