package pl.dotnet.main.dto.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventTicketsDTo {

    @NotNull
    private Long eventId;
    @NotNull
    private Long availableTickets;
    @NotNull
    private Double ticketPrice;
}
