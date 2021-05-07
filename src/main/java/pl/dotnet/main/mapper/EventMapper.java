package pl.dotnet.main.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dto.EventDTO;
import pl.dotnet.main.dto.UserDTO;

import java.util.List;
import java.util.Optional;


@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "owner", source = "event", qualifiedByName = "getOwner")
    EventDTO eventToDto(Event event);

    @Mapping(target = "owner", source = "event", qualifiedByName = "getOwner")
    List<EventDTO> eventToDtoList(List<Event> event);

    @Named("getOwner")
    default UserDTO getOwner(Event event) {
        return UserDTO.builder()
                .surname(event.getOwner().getSurname())
                .name(event.getOwner().getName())
                .id(event.getOwner().getUserId())
                .email(event.getOwner().getEmail())
                .build();
    }
}
