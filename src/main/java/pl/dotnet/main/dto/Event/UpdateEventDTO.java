package pl.dotnet.main.dto.Event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateEventDTO {
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
}
