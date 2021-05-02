package pl.dotnet.main.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dto.EventDTO;

import java.util.List;


@Mapper
public interface EventMapper {

    @Mapping(target = "eventId", source = "eventId")
    @Mapping(target = "ownerUsername", source = "owner.username")
    EventDTO eventToDto(Event event);

    @Mapping(target = "eventId", source = "eventId")
    @InheritInverseConfiguration
    Event dtoToEvent(EventDTO createEventDTO);

    @Mapping(target = "eventId", source = "eventId")
    @Mapping(target = "ownerUsername", source = "owner.username")
    List<EventDTO> eventToDtoList(List<Event> employees);

}
