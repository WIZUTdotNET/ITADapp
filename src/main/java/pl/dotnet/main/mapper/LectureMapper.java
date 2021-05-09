package pl.dotnet.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.dotnet.main.dao.model.Lecture;
import pl.dotnet.main.dto.Lecture.LectureDTO;
import pl.dotnet.main.dto.Speaker.SpeakerDTO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")

public interface LectureMapper {

    @Mapping(target = "eventId", source = "lecture.event.eventId")
    @Mapping(target = "speakers", source = "lecture", qualifiedByName = "getSpeakersDTO")
    LectureDTO lectureToDTO(Lecture lecture);

    @Named("getSpeakersDTO")
    default List<SpeakerDTO> getSpeakersDTO(Lecture lecture) {
        if (lecture.getSpeakers() == null)
            return Collections.emptyList();
        return lecture.getSpeakers().stream()
                .map(speaker -> SpeakerDTO.builder()
                        .speakerId(speaker.getSpeakerId())
                        .name(speaker.getName())
                        .surname(speaker.getSurname())
                        .description(speaker.getDescription())
                        .eventId(speaker.getEvent().getEventId())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
