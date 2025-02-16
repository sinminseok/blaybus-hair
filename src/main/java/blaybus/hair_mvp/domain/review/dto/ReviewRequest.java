package blaybus.hair_mvp.domain.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ReviewRequest {
    private Integer starPoint;
    private String content;
    private UUID designerId;
    private UUID reviewId;
}
