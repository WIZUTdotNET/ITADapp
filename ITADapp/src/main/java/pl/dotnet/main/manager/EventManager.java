package pl.dotnet.main.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dotnet.main.model.Event;
import pl.dotnet.main.repository.EventRepository;

import javax.swing.text.html.Option;

@Service
public class EventManager {

    private EventRepository eventRepository;

    @Autowired
    public EventManager(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Optional<Event> getAllEvent() {
        return eventRepository.findAll();
    }

}
