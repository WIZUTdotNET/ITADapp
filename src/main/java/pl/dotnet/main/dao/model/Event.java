package pl.dotnet.main.dao.model;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @NotEmpty(message = "Event name cannot be empty or NULL")
    private String name;

    private String description;

    private Instant startDate;

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

    public void addPartnerToEvent(EventPartner partner) {
        List<EventPartner> partners = getPartners();
        partners.add(partner);
        setPartners(partners);
    }

    public void removePartnerFromEvent(EventPartner partner) {
        List<EventPartner> partners = getPartners();
        partners.remove(partner);
        setPartners(partners);
    }

    public void addLectureToEvent(Lecture lecture) {
        List<Lecture> lectures = getLectures();
        lectures.add(lecture);
        setLectures(lectures);
    }

    public void removeLectureFromEvent(Lecture lecture) {
        List<Lecture> lectures = getLectures();
        lectures.remove(lecture);
        setLectures(lectures);
    }

    public void registerUser(User user) {
        List<User> registeredUsers = getRegisteredUsers();
        registeredUsers.add(user);
        setAttendedUsers(registeredUsers);
        setBookedTickets(getBookedTickets() + 1);
    }

    public void markUserAsAttended(User user) {
        List<User> attendedUsers = getAttendedUsers();
        attendedUsers.add(user);
        setAttendedUsers(attendedUsers);
    }
}
