package pl.dotnet.main.dao.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureId;

    @NotEmpty(message = "Lecture name cannot be empty or NULL")
    private String name;

    @NotNull
    private String description;

    private Instant startDate;

    @NotNull
    private Long availableSeats;

    @NotNull
    private Long takenSeats;

    @ManyToOne
    private Event event;

    @ManyToMany
    private List<Speaker> speakers;

    @ManyToMany
    private List<User> registeredUsers;

    @ManyToMany
    private List<User> attendedUsers;
}
