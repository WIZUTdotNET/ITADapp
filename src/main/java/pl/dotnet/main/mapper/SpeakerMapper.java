package pl.dotnet.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.dotnet.main.dao.model.Speaker;
import pl.dotnet.main.dto.SpeakerDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpeakerMapper {

    @Mapping(target = "eventId", source = "speaker.event.eventId")
    SpeakerDTO speakerToDto(Speaker speaker);

    @Mapping(target = "eventId", source = "speaker.event.eventId")
    List<SpeakerDTO> speakerToDto(List<Speaker> speaker);
}
