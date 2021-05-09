package pl.dotnet.main.dao.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Speaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long speakerId;

    @NotEmpty(message = "Name cant be empty")
    private String name;

    @NotEmpty(message = "Surname cant be empty")
    private String surname;

    @NotNull
    private String description;

    @ManyToOne(cascade = CascadeType.DETACH)
    private Event event;

    @ManyToMany(cascade = CascadeType.DETACH)
    private List<Lecture> lectures;

    public void addLecture(Lecture lecture) {
        lectures.add(lecture);
    }

    public void removeLecture(Lecture lecture) {
        lectures.remove(lecture);
    }
}
