package pl.dotnet.main.dto.EventPartner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventPartnerDTO {

    @NotNull
    private Long eventPartnerId;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Long eventId;
}
