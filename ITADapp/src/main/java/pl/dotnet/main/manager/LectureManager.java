package pl.dotnet.main.manager;

import org.springframework.beans.factory.annotation.Autowired;
import pl.dotnet.main.dao.model.Lecture;
import pl.dotnet.main.dao.repository.LectureRepository;

import java.util.Optional;

public class LectureManager {
    private LectureRepository lectureRepository;

    @Autowired
    public LectureManager(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public Iterable<Lecture> findAll() {
        return lectureRepository.findAll();
    }

    public Optional<Lecture> findById(Long id) {
        return lectureRepository.findById(id);
    }

    public Lecture save(Lecture lecture) {
        return lectureRepository.save(lecture);
    }

    public void deleteById(Long id) {
        lectureRepository.deleteById(id);
    }
}
