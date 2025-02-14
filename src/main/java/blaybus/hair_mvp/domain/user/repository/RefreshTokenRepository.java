package blaybus.hair_mvp.domain.user.repository;

import blaybus.hair_mvp.domain.user.entity.RefreshToken;
import blaybus.hair_mvp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByUser(final User user);

    Optional<RefreshToken> findByToken(final String token);

}
