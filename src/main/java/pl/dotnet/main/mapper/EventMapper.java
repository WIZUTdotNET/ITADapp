package pl.dotnet.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dto.EventDTO;
import pl.dotnet.main.dto.UserDTO;


@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "owner", source = "event", qualifiedByName = "getOwner")
    EventDTO eventToDto(Event event);

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
