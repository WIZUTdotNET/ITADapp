package pl.dotnet.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.Speaker;
import pl.dotnet.main.dto.Event.DetailedEventDTO;
import pl.dotnet.main.dto.Event.EventDTO;
import pl.dotnet.main.dto.EventPartner.EventPartnerDTO;
import pl.dotnet.main.dto.Lecture.LectureDTO;
import pl.dotnet.main.dto.Speaker.SpeakerDTO;
import pl.dotnet.main.dto.User.UserDTO;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "owner", source = "event", qualifiedByName = "getOwner")
    EventDTO eventToDto(Event event);

    @Mapping(target = "owner", source = "event", qualifiedByName = "getOwner")
    @Mapping(target = "partners", source = "event", qualifiedByName = "getPartners")
    @Mapping(target = "lectures", source = "event", qualifiedByName = "getLectures")
    DetailedEventDTO eventToDetailedDto(Event event);

    @Named("getOwner")
    default UserDTO getOwner(Event event) {
        return UserDTO.builder()
                .surname(event.getOwner().getSurname())
                .name(event.getOwner().getName())
                .userId(event.getOwner().getUserId())
                .email(event.getOwner().getEmail())
                .build();
    }

    @Named("getPartners")
    default List<EventPartnerDTO> getPartners(Event event) {
        return event.getPartners().stream()
                .map(eventPartner -> EventPartnerDTO.builder()
                        .eventPartnerId(eventPartner.getPartnerId())
                        .name(eventPartner.getName())
                        .description(eventPartner.getDescription())
                        .eventId(event.getEventId())
                        .build())
                .collect(Collectors.toList());
    }

    @Named("getLectures")
    default List<LectureDTO> getLectures(Event event) {
        return event.getLectures().stream()
                .map(lecture -> LectureDTO.builder()
                        .lectureId(lecture.getLectureId())
                        .name(lecture.getName())
                        .description(lecture.getDescription())
                        .startDate(lecture.getStartDate().toString())
                        .eventId(event.getEventId())
                        .speakers(lecture.getSpeakers().stream()
                                .map(speaker -> SpeakerDTO.builder()
                                        .speakerId(speaker.getSpeakerId())
                                        .name(speaker.getName())
                                        .surname(speaker.getSurname())
                                        .description(speaker.getDescription())
                                        .eventId(speaker.getEvent().getEventId())
                                        .build())
                                .collect(Collectors.toList()))
                        .speakersIds(lecture.getSpeakers().stream()
                                .map(Speaker::getSpeakerId)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
