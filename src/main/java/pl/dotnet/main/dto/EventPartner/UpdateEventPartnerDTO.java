package pl.dotnet.main.dto.EventPartner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventPartnerDTO {

    @NotNull
    private Long eventPartnerId;

    @NotNull
    private String name;

    @NotNull
    private String description;
}