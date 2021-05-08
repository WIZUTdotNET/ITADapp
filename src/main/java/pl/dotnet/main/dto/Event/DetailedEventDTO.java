package pl.dotnet.main.dto.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dotnet.main.dto.EventPartner.EventPartnerDTO;
import pl.dotnet.main.dto.Lecture.LectureDTO;
import pl.dotnet.main.dto.User.UserDTO;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailedEventDTO {

    @NotNull
    private Long eventId;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String startDate;

    @NotNull
    private Long availableTickets;

    @NotNull
    private Long bookedTickets;

    @NotNull
    private Double ticketPrice;

    @NotNull
    private UserDTO owner;

    @NotNull
    private List<EventPartnerDTO> partners;

    @NotNull
    private List<LectureDTO> lectures;
}
