package pl.dotnet.main.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.EventPartner;

import java.util.List;

public interface EventPartnerRepository extends JpaRepository<EventPartner, Long> {
    List<EventPartner> findAllBySponsoredEvent(Event event);
}
