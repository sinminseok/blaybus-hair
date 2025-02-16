package blaybus.hair_mvp.domain.review.service;

import blaybus.hair_mvp.domain.designer.entity.Designer;
import blaybus.hair_mvp.domain.designer.repository.DesignerRepository;
import blaybus.hair_mvp.domain.review.dto.ReviewRequest;
import blaybus.hair_mvp.domain.review.dto.ReviewResponse;
import blaybus.hair_mvp.domain.review.entity.Review;
import blaybus.hair_mvp.domain.review.mapper.ReviewMapper;
import blaybus.hair_mvp.domain.review.repository.ReviewRepository;
import blaybus.hair_mvp.domain.user.entity.User;
import blaybus.hair_mvp.domain.user.repository.UserRepository;
import blaybus.hair_mvp.utils.OptionalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static blaybus.hair_mvp.constants.ErrorMessages.NOT_EXIST_DESIGNER_ID_MESSAGE;
import static blaybus.hair_mvp.constants.ErrorMessages.NOT_EXIST_USER_EMAIL_MESSAGE;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final DesignerRepository designerRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public void save(final ReviewRequest request, final String userEmail) {
        Review review = reviewMapper.toEntity(request);
        Designer designer = OptionalUtil.getOrElseThrow(designerRepository.findById(request.getDesignerId()), NOT_EXIST_DESIGNER_ID_MESSAGE);
        User user = OptionalUtil.getOrElseThrow(userRepository.findByEmail(userEmail), NOT_EXIST_USER_EMAIL_MESSAGE);
        review.setDesigner(designer);
        review.setUser(user);
        reviewRepository.save(review);
        designer.addReview(review);
        user.addReview(review);
    }

    public List<ReviewResponse> findAllByDesignerId(final UUID designerId) {
        Designer designer = OptionalUtil.getOrElseThrow(designerRepository.findById(designerId), NOT_EXIST_DESIGNER_ID_MESSAGE);
        return reviewRepository.findAllByDesignerId(designerId).stream()
                .map(review -> reviewMapper.toResponse(review, designer))
                .collect(Collectors.toList());
    }


}
