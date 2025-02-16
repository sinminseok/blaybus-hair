package blaybus.hair_mvp.domain.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReviewResponse {
    private LocalDateTime createdAt;
    private Integer starPoint;
    private String content;
    private String designerName;
    private String designerProfile;
}
