package pl.dotnet.main.dto.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventDTO {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Instant startTime;

    @NotNull
    private Long availableTickets;

    @NotNull
    private Double ticketPrice;
}
