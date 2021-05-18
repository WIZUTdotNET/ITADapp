package pl.dotnet.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.dotnet.main.dao.model.Ticket;
import pl.dotnet.main.dto.Ticket.TicketDTO;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(target = "eventId", source = "ticket.event.eventId")
    @Mapping(target = "eventName", source = "ticket.event.name")
    @Mapping(target = "userId", source = "ticket.user.userId")
    TicketDTO ticketToDto(Ticket ticket);
}
