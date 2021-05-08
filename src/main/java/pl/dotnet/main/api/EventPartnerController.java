package pl.dotnet.main.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.dto.EventPartner.CreateEventPartnerDTO;
import pl.dotnet.main.dto.EventPartner.EventPartnerDTO;
import pl.dotnet.main.dto.EventPartner.UpdateEventPartnerDTO;
import pl.dotnet.main.service.EventPartnerService;

import java.util.List;

@RestController
@RequestMapping("/api/eventPartner")
@AllArgsConstructor
public class EventPartnerController {

    private final EventPartnerService eventPartnerService;

    @PostMapping
    public ResponseEntity<String> addPartner(@RequestBody CreateEventPartnerDTO eventPartnerDTO) {
        return eventPartnerService.addPartner(eventPartnerDTO);
    }

    @PutMapping
    public ResponseEntity<String> updatePartner(@RequestBody UpdateEventPartnerDTO eventPartnerDTO) {
        return eventPartnerService.editPartner(eventPartnerDTO);
    }

    @DeleteMapping
    public ResponseEntity<String> deletePartner(@RequestParam Long partnerId) {
        return eventPartnerService.deletePartner(partnerId);
    }

    @GetMapping("/findInEvent")
    public ResponseEntity<List<EventPartnerDTO>> getInEvent(@RequestParam Long eventId) {
        return eventPartnerService.findByEventId(eventId);
    }

    @GetMapping("/findById")
    public ResponseEntity<EventPartnerDTO> getById(@RequestParam Long partnerId) {
        return eventPartnerService.findById(partnerId);
    }
}
