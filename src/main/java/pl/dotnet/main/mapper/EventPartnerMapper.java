package pl.dotnet.main.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.dotnet.main.dao.model.EventPartner;
import pl.dotnet.main.dto.EventPartnerDto;

@Mapper
public interface EventPartnerMapper {

    @Mapping(target = "id", source = "eventPartnerId")
    EventPartnerDto eventPartnerToDto(EventPartner eventPartner);

    @Mapping(target = "eventPartnerId", source = "id")
    @InheritInverseConfiguration
    EventPartner dtoToEventPartner(EventPartnerDto eventPartnerDto);
}
