package pl.dotnet.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventDTO {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private LocalDateTime startTime;
}
