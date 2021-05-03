package pl.dotnet.main.dao.model;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @NotEmpty(message = "Event name cannot be empty or NULL")
    private String name;

    private String description;

    private Instant startDate;
    private Instant endDate;

    @Nullable
    private Long availableTickets;

    @Nullable
    private Long bookedTickets;

    @Nullable
    private Double ticketPrice;

    @ManyToOne
    private User owner;

    @OneToMany
    private List<EventPartner> partners;

    @OneToMany
    private List<Lecture> lectures;

    @ManyToMany
    private List<User> registeredUsers;

    @ManyToMany
    private List<User> attendedUsers;
}
