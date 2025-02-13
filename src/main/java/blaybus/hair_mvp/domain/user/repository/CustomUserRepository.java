package blaybus.hair_mvp.domain.user.repository;

import blaybus.hair_mvp.domain.user.entity.User;

import java.util.Optional;

public interface CustomUserRepository {

    Optional<User> findByEmail(final String email);
}
