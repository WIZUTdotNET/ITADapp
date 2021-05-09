package pl.dotnet.main.mapper;

import org.mapstruct.Mapper;
import pl.dotnet.main.dao.model.Ticket;
import pl.dotnet.main.dto.Ticket.CreateTicketDTO;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    CreateTicketDTO ticketToDto(Ticket ticket);
}
