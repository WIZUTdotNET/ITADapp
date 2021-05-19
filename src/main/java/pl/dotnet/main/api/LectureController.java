package pl.dotnet.main.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.dto.Lecture.CreateLectureDTO;
import pl.dotnet.main.dto.Lecture.LectureDTO;
import pl.dotnet.main.dto.Lecture.UpdateLectureDTO;
import pl.dotnet.main.service.LectureService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lecture")
@AllArgsConstructor
public class LectureController {
    private final LectureService lectureService;

    @PostMapping
    public ResponseEntity<LectureDTO> addLecture(@RequestBody CreateLectureDTO lectureDTO) {
        return lectureService.addLecture(lectureDTO);
    }

    @PostMapping("/addSpeaker")
    public ResponseEntity<String> addSpeakerToLecture(@RequestParam Long speakerId, Long lectureId) {
        return lectureService.addSpeakerToLecture(speakerId, lectureId);
    }

    @DeleteMapping("/removeSpeaker")
    public ResponseEntity<String> removeSpeakerFromLecture(@RequestParam Long speakerId, Long lectureId) {
        return lectureService.removeSpeakerFromLecture(speakerId, lectureId);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteLecture(@RequestParam Long lectureId) {
        return lectureService.deleteLecture(lectureId);
    }

    @PutMapping
    public ResponseEntity<String> updateLecture(@RequestBody UpdateLectureDTO lectureDTO) {
        return lectureService.editLectureById(lectureDTO);
    }

    @GetMapping("/getLectureFromEvent")
    public List<LectureDTO> getLecturesFromEvent(@RequestParam Long eventId) {
        return lectureService.findLecturesByEventId(eventId);
    }

    @GetMapping("/getLecture")
    public LectureDTO getLectureById(@RequestParam Long lectureId) {
        return lectureService.findLectureById(lectureId);
    }

    @PostMapping("/MarkPresents")
    public ResponseEntity<Object> markUserAsAttendedOnLecture(@RequestParam UUID userUUID, Long lectureId) {
        return lectureService.markUserAsAttended(userUUID, lectureId);
    }
}
