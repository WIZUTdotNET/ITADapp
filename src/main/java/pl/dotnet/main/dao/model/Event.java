package pl.dotnet.main.dao.model;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @NotEmpty(message = "Event name cannot be empty or NULL")
    private String name;

    //    @NotNull
    private String description;

    private Instant startDate;
    private Instant endDate;

    //    @NotNull
    @Nullable
    private Long availableTickets;

    //    @NotNull
    @Nullable
    private Long bookedTickets;

    //    @NotNull
    @Nullable
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
