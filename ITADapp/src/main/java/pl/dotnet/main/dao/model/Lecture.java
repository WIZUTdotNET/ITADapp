package pl.dotnet.main.dao.model;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dotnet.main.dao.model.Event;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureId;

    @NotEmpty(message = "Lecture name cannot be empty or NULL")
    private String lectureName;

    @Nullable
    private String lectureDescription;

    private Timestamp levtireStartDate;
    private Timestamp levtireEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventID", referencedColumnName = "eventID")
    private Event event;
}
