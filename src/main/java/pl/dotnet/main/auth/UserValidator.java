package pl.dotnet.main.auth;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dotnet.main.dao.model.User;
import pl.dotnet.main.dao.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public boolean valideteUser(User user) {
        Optional<User> usetFromDBUsername = userRepository.findByUsername(user.getUsername());
        Optional<User> usetFromDBEmail = userRepository.findByEmail(user.getEmail());

        return !usetFromDBUsername.isPresent() && !usetFromDBEmail.isPresent();
    }

}
