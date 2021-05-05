package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dotnet.main.dao.model.User;
import pl.dotnet.main.dao.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public boolean validateUser(User user) {
        Optional<User> userFromDBUsername = userRepository.findByUsername(user.getUsername());
        Optional<User> userFromDBEmail = userRepository.findByEmail(user.getEmail());

        return userFromDBUsername.isEmpty() && userFromDBEmail.isEmpty();
    }

}
