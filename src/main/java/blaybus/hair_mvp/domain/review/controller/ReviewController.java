package blaybus.hair_mvp.domain.review.controller;

import blaybus.hair_mvp.auth.SecurityContextHelper;
import blaybus.hair_mvp.domain.review.dto.ReviewRequest;
import blaybus.hair_mvp.domain.review.dto.ReviewResponse;
import blaybus.hair_mvp.domain.review.service.ReviewService;
import blaybus.hair_mvp.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final SecurityContextHelper securityContextHelper;

    @PostMapping
    public ResponseEntity<?> saveReview(@RequestBody final ReviewRequest reviewRequest) {
        reviewService.save(reviewRequest, securityContextHelper.getEmailInToken());
        SuccessResponse response = new SuccessResponse(true, "후기 등록 성공", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/designer")
    public ResponseEntity<?> findAllByDesigner(@RequestParam final UUID designerId) {
        List<ReviewResponse> reviews = reviewService.findAllByDesignerId(designerId);
        SuccessResponse response = new SuccessResponse(true, "디자이너 후기 조회 성공", reviews);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
