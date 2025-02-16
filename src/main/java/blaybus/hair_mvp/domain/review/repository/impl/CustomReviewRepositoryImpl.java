package blaybus.hair_mvp.domain.review.repository.impl;

import blaybus.hair_mvp.domain.review.entity.QReview;
import blaybus.hair_mvp.domain.review.entity.Review;
import blaybus.hair_mvp.domain.review.repository.CustomReviewRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomReviewRepositoryImpl implements CustomReviewRepository {

    private final JPAQueryFactory query;
    private QReview qReview;


    @Override
    public List<Review> findAllByDesignerId(UUID designerId) {
        return query
                .selectFrom(qReview.review)
                .where(qReview.review.designer.id.eq(designerId))
                .fetch();
    }

    @Override
    public List<Review> findAllByUserId(UUID userId) {
        return query
                .selectFrom(qReview.review)
                .where(qReview.review.user.id.eq(userId))
                .fetch();
    }
}
