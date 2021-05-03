package pl.dotnet.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class EventDTO {

    private Long eventId;
    private String name;
    private String description;
    private Instant startDate;
    private String ownerUsername;
}