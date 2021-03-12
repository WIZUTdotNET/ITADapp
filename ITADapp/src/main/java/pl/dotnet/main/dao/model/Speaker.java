package pl.dotnet.main.dao.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
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

    @ManyToOne
    private Lecture lecture;
}
