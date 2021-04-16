package pl.dotnet.main.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.dotnet.main.dao.model.Speaker;
import pl.dotnet.main.dto.SpeakerDto;

@Mapper
public interface SpeakerMapper {

    @Mapping(target = "id", source = "speakerId")
    SpeakerDto speakerToDto(Speaker speaker);

    @Mapping(target = "speakerId", source = "id")
    @InheritInverseConfiguration
    Speaker dtoToSpeaker(SpeakerDto speakerDto);

}
