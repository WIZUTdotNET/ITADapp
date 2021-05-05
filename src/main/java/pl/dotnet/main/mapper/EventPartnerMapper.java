package pl.dotnet.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.dotnet.main.dao.model.EventPartner;
import pl.dotnet.main.dto.EventPartnerDTO;

@Mapper(componentModel = "spring")
public interface EventPartnerMapper {

    @Mapping(target = "eventId", source = "eventPartner.sponsoredEvent.eventId")
    EventPartnerDTO eventPartnerToDto(EventPartner eventPartner);
}
