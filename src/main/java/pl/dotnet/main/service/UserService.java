package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.User;
import pl.dotnet.main.dao.repository.UserRepository;
import pl.dotnet.main.expections.NotFoundRequestException;
import pl.dotnet.main.expections.UnauthorizedRequestException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public String getCurrentUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    public boolean validateUser(User user) {
        Optional<User> userFromDBUsername = userRepository.findByUsername(user.getUsername());
        Optional<User> userFromDBEmail = userRepository.findByEmail(user.getEmail());

        return userFromDBUsername.isEmpty() && userFromDBEmail.isEmpty();
    }

    public boolean isCurrentUserNotTheOwnerOfThisEvent(Event event) {
        User currentUser = userRepository.findByUsername(getCurrentUserName()).orElseThrow(() -> new NotFoundRequestException("User not found"));
        if (!userRepository.findById(event.getOwner().getUserId()).orElseThrow(() -> new UnauthorizedRequestException("User is not the owner of this event")).equals(currentUser)) {
            throw new UnauthorizedRequestException("User is not the owner of this event");
        } else return false;
    }
}
