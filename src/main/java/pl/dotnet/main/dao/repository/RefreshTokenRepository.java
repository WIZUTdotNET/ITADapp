package pl.dotnet.main.dao.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.dotnet.main.dao.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);
}
