package blaybus.hair_mvp.domain.user.repository.impl;

import blaybus.hair_mvp.domain.user.entity.QUser;
import blaybus.hair_mvp.domain.user.entity.User;
import blaybus.hair_mvp.domain.user.repository.CustomUserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory query;
    private QUser user = QUser.user;

    @Override
    public Optional<User> findByEmail(final String email) {
        return Optional.ofNullable(
                query.selectFrom(user)
                        .where(user.email.eq(email))
                        .fetchOne()
        );
    }

}
