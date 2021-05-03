package pl.dotnet.main.dao.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureId;

    @NotEmpty(message = "Lecture name cannot be empty or NULL")
    private String name;

    @NotNull
    private String description;

    private Instant startDate;
    private Instant endDate;

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
