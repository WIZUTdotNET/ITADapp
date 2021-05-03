//package pl.dotnet.main.mapper;
//
//import org.mapstruct.InheritInverseConfiguration;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import pl.dotnet.main.dao.model.Ticket;
//import pl.dotnet.main.dto.TicketDTO;
//
//@Mapper
//public interface TicketMapper {
//
//    @Mapping(target = "id", source = "ticketId")
//    TicketDTO ticketToDto(Ticket ticket);
//
//    @Mapping(target = "ticketId", source = "id")
//    @InheritInverseConfiguration
//    Ticket dtoToTicket(TicketDTO ticketDto);
//}
