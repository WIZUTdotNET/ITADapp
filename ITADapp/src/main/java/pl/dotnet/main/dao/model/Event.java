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

    @OneToMany(mappedBy = "event")
    private List<EventPartner> partners;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private List<User> moderators;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private List<User> participants;
}
