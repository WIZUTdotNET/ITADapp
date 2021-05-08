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
    private List<Event> registeredOnEvents;

    @ManyToMany
    private List<Event> attendedEvents;

    private Instant created;

    private Boolean isActive;

    public void addEventToOwned(Event event) {
        List<Event> ownedEvents = getOwnedEvents();
        ownedEvents.add(event);
        setOwnedEvents(ownedEvents);
    }

    public void removeEventFromOwned(Event event) {
        List<Event> ownedEvents = getOwnedEvents();
        ownedEvents.remove(event);
        setOwnedEvents(ownedEvents);
    }

    public void registerOnEvent(Event event) {
        List<Event> registeredOnEvents = getRegisteredOnEvents();
        registeredOnEvents.add(event);
        setRegisteredOnEvents(registeredOnEvents);
    }

    public void attendEvent(Event event) {
        List<Event> attendedEvents = getAttendedEvents();
        attendedEvents.add(event);
        setAttendedEvents(attendedEvents);
    }
}
