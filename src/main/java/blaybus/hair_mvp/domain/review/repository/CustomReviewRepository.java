package blaybus.hair_mvp.domain.review.repository;

import blaybus.hair_mvp.domain.review.entity.Review;

import java.util.List;
import java.util.UUID;

public interface CustomReviewRepository {

    List<Review> findAllByDesignerId(final UUID designerId);

    List<Review> findAllByUserId(final UUID designerId);

}
