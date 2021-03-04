package pl.dotnet.main.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.dao.model.Lecture;
import pl.dotnet.main.manager.LectureManager;

import java.util.Optional;

@RestController
@RequestMapping("/api/lecture")
public class LectureAPI {
    private final LectureManager lectureManager;

    @Autowired
    public LectureAPI(LectureManager lectureManager) {
        this.lectureManager = lectureManager;
    }

    @GetMapping("/all")
    public Iterable<Lecture> getALl() {
        return lectureManager.findAll();
    }

    @GetMapping
    public Optional<Lecture> getById(@RequestParam Long id) {
        return lectureManager.findById(id);
    }

    @PostMapping
    public Lecture addLecture(@RequestBody Lecture lecture) {
        return lectureManager.save(lecture);
    }

    @PutMapping
    public Lecture saveLecture(@RequestBody Lecture lecture) {
        return lectureManager.save(lecture);
    }

    @DeleteMapping
    public void deleteLecture(@RequestParam Long id) {
        lectureManager.deleteById(id);
    }
}
