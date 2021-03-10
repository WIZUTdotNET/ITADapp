package pl.dotnet.main.dao.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", referencedColumnName = "eventId")
    private Event event;

    @OneToMany(mappedBy = "lecture")
    private List<Speaker> speakers;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> participants;
}
