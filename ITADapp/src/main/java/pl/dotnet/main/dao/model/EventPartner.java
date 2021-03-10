package pl.dotnet.main.dao.model;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EventPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventPartnerId;

    @NotEmpty()
    private String name;

    @NotNull
    private String description;

    @Nullable
    private String profileImageURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId")
    private Event event;

}
