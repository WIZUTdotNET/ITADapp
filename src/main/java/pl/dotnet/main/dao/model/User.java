package pl.dotnet.main.dao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userUUID;

    @NotEmpty(message = "User username cannot be empty or NULL")
    private String username;

    @NotEmpty(message = "User password cannot be empty or NULL")
    private String password;

    private String name;
    private String surname;

    @Email
    private String email;

    @OneToMany
    private List<Event> ownedEvents;

    @ManyToMany
    private List<Ticket> registeredOnEvents;

    @ManyToMany
    private List<Ticket> attendedEvents;

    private Instant created;

    private Boolean isActive;

    public void addEventToOwned(Event event) {
        ownedEvents.add(event);
    }

    public void removeEventFromOwned(Event event) {
        ownedEvents.remove(event);
    }

    public void registerOnEvent(Ticket ticket) {
        registeredOnEvents.add(ticket);
    }

    public void attendEvent(Ticket ticket) {
        attendedEvents.add(ticket);
    }
}
