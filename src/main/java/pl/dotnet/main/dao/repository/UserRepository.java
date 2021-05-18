package pl.dotnet.main.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dotnet.main.dao.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String username);

    Optional<User> findByUserUUID(UUID uuid);
}
