package pl.dotnet.main.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dto.EventDto;

@Mapper
public interface EventMapper {

    @Mapping(target = "id", source = "eventId")
    EventDto eventToDto(Event event);

    @Mapping(target = "eventId", source = "id")
    @InheritInverseConfiguration
    Event dtoToEvent(EventDto eventDto);
}
