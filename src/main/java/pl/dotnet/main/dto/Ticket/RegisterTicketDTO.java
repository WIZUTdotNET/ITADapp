package pl.dotnet.main.dto.Ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterTicketDTO {

    @NotNull
    private Long eventId;

    @NotNull
    private String name;

    @NotNull
    private String surname;
}
