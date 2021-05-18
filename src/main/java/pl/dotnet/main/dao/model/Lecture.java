package pl.dotnet.main.dao.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureId;

    @NotEmpty(message = "Lecture name cannot be empty or NULL")
    private String name;

    @NotNull
    private String description;

    private Instant startDate;

    @ManyToOne
    private Event event;

    @ManyToMany
    private List<Speaker> speakers;

    @ManyToMany
    private List<Ticket> attendedUsers;

    public void addSpeakerToLecture(Speaker speaker) {
        speakers.add(speaker);
    }

    public void removeSpeakerFromLecture(Speaker speaker) {
        speakers.remove(speaker);
    }

    public void markTicketAsAttended(Ticket ticket) {
        attendedUsers.add(ticket);
    }
}
