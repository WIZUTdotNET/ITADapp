package pl.dotnet.main.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @NotEmpty(message = "Event name cannot be empty or NULL")
    private String name;

    @NotNull
    private String description;

    private Instant startDate;
    private Instant endDate;

    @NotNull
    private Long availableTickets;

    @NotNull
    private Long bookedTickets;

    @NotNull
    private Long ticketPrice;

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
