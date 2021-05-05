package pl.dotnet.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDTO {

    @NotNull
    private Long eventId;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String startDate;

    @NotNull
    private UserDTO owner;
}
