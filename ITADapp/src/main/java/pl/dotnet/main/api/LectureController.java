package pl.dotnet.main.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.dao.model.Lecture;
import pl.dotnet.main.service.LectureService;

import java.util.Optional;

@RestController
@RequestMapping("/api/lecture")
public class LectureController {
    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("/all")
    public Iterable<Lecture> getALl() {
        return lectureService.findAll();
    }

    @GetMapping
    public Optional<Lecture> getById(@RequestParam Long id) {
        return lectureService.findById(id);
    }

    @PostMapping
    public Lecture addLecture(@RequestBody Lecture lecture) {
        return lectureService.save(lecture);
    }

    @PutMapping
    public Lecture saveLecture(@RequestBody Lecture lecture) {
        return lectureService.save(lecture);
    }

    @DeleteMapping
    public void deleteLecture(@RequestParam Long id) {
        lectureService.deleteById(id);
    }
}
