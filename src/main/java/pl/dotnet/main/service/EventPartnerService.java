package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dotnet.main.dao.repository.EventPartnerRepository;
import pl.dotnet.main.dao.repository.EventRepository;
import pl.dotnet.main.mapper.EventPartnerMapper;

@Service
@AllArgsConstructor
public class EventPartnerService {

    private final EventPartnerRepository eventPartnerRepository;
    private final EventPartnerMapper eventPartnerMapper;
    private final EventRepository eventRepository;
    private final UserService userService;

}
