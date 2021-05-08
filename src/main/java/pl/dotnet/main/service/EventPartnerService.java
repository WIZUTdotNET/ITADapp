package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.EventPartner;
import pl.dotnet.main.dao.repository.EventPartnerRepository;
import pl.dotnet.main.dao.repository.EventRepository;
import pl.dotnet.main.dto.EventPartner.CreateEventPartnerDTO;
import pl.dotnet.main.dto.EventPartner.EventPartnerDTO;
import pl.dotnet.main.dto.EventPartner.UpdateEventPartnerDTO;
import pl.dotnet.main.mapper.EventPartnerMapper;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Service
@AllArgsConstructor
@Transactional
public class EventPartnerService {

    private final EventPartnerRepository eventPartnerRepository;
    private final EventPartnerMapper eventPartnerMapper;
    private final EventRepository eventRepository;
    private final UserService userService;

    public ResponseEntity<List<EventPartnerDTO>> findByEventId(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        List<EventPartnerDTO> eventPartnerDTOList = eventPartnerRepository.findAllBySponsoredEvent(event).stream()
                .map(eventPartnerMapper::eventPartnerToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(eventPartnerDTOList, OK);
    }

    public ResponseEntity<EventPartnerDTO> findById(Long partnerId) {
        EventPartnerDTO eventPartnerDTO = eventPartnerMapper.eventPartnerToDto(eventPartnerRepository.findById(partnerId).orElse(null));
        return new ResponseEntity<>(eventPartnerDTO, OK);
    }

    public ResponseEntity<String> addPartner(CreateEventPartnerDTO eventPartnerDTO) {
        Event event = eventRepository.findById(eventPartnerDTO.getEventId()).orElseThrow();
        if (userService.isCurrentUserNotTheOwnerOfThisEvent(event))
            return new ResponseEntity<>("", FORBIDDEN);

        EventPartner newEventPartner = EventPartner.builder()
                .name(eventPartnerDTO.getName())
                .description(eventPartnerDTO.getDescription())
                .sponsoredEvent(event)
                .build();

        event.addPartnerToEvent(newEventPartner);
        eventPartnerRepository.save(newEventPartner);
        eventRepository.save(event);
        return new ResponseEntity<>("", OK);
    }

    public ResponseEntity<String> editPartner(UpdateEventPartnerDTO eventPartnerDTO) {
        EventPartner oldEventPartner = eventPartnerRepository.findById(eventPartnerDTO.getEventPartnerId()).orElseThrow();

        Event event = eventRepository.findById(oldEventPartner.getEventPartnerId()).orElseThrow();
        if (userService.isCurrentUserNotTheOwnerOfThisEvent(event))
            return new ResponseEntity<>("", FORBIDDEN);

        EventPartner newEventPartner = EventPartner.builder()
                .eventPartnerId(oldEventPartner.getEventPartnerId())
                .name(eventPartnerDTO.getName())
                .description(eventPartnerDTO.getDescription())
                .sponsoredEvent(event)
                .build();

        eventPartnerRepository.save(newEventPartner);
        return new ResponseEntity<>("", OK);
    }

    public ResponseEntity<String> deletePartner(Long eventPartnerId) {
        EventPartner eventPartner = eventPartnerRepository.findById(eventPartnerId).orElseThrow();
        Event event = eventRepository.findById(eventPartner.getSponsoredEvent().getEventId()).orElseThrow();

        if (userService.isCurrentUserNotTheOwnerOfThisEvent(event))
            return new ResponseEntity<>("", FORBIDDEN);

        event.removePartnerFromEvent(eventPartner);
        eventRepository.save(event);
        eventPartnerRepository.deleteById(eventPartnerId);
        return new ResponseEntity<String>("", OK);
    }
}
