package pl.dotnet.main.dto.EventPartner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventPartnerDTO {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Long eventId;
}
