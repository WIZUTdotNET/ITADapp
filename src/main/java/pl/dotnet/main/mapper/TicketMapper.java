package pl.dotnet.main.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.dotnet.main.dao.model.Ticket;
import pl.dotnet.main.dto.TicketDto;

@Mapper
public interface TicketMapper {

    @Mapping(target = "id", source = "ticketId")
    TicketDto ticketToDto(Ticket ticket);

    @Mapping(target = "ticketId", source = "id")
    @InheritInverseConfiguration
    Ticket dtoToTicket(TicketDto ticketDto);
}
