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

    @ManyToOne(cascade = CascadeType.DETACH)
    private User owner;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventPartner> partners;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Speaker> speakers;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lecture> lectures;

    @ManyToMany(cascade = CascadeType.DETACH)
    private List<Ticket> registeredUsers;

    @ManyToMany(cascade = CascadeType.DETACH)
    private List<Ticket> attendedUsers;

    public void addPartnerToEvent(EventPartner partner) {
        partners.add(partner);
    }

    public void removePartnerFromEvent(EventPartner partner) {
        partners.remove(partner);
    }

    public void addSpeakerToEvent(Speaker speaker) {
        speakers.add(speaker);
    }

    public void removeSpeakerFromEvent(Speaker speaker) {
        speakers.remove(speaker);
    }

    public void addLectureToEvent(Lecture lecture) {
        lectures.add(lecture);
    }

    public void removeLectureFromEvent(Lecture lecture) {
        lectures.remove(lecture);
    }

    public void registerTicket(Ticket ticket) {
        registeredUsers.add(ticket);
        bookedTickets++;
    }

    public void markTicketAsAttended(Ticket ticket) {
        attendedUsers.add(ticket);
    }
}
