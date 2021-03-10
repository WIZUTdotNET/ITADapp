package pl.dotnet.main.dao.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotEmpty(message = "User password cannot be empty or NULL")
    private String password;

    @NotEmpty(message = "User username cannot be empty or NULL")
    private String username;

    @Email
    private String email;

    @OneToMany(mappedBy = "owner")
    private List<Event> ownedEvents;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId")
    private List<Event> moderatedEvents;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId")
    private List<Event> attendedEvents;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "lectureId")
    private List<Lecture> attendedLectures;
}
