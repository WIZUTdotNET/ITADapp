package pl.dotnet.main.dto.Ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketDTO {

    @NotNull
    private Long price;

    @NotNull
    private Boolean isPayed;

    @NotNull
    private String name;

    @NotNull
    private String surname;

}
