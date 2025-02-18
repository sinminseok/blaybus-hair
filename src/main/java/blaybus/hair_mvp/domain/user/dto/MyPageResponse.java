package blaybus.hair_mvp.domain.user.dto;

import blaybus.hair_mvp.domain.reservation.dto.ReservationResponse;
import blaybus.hair_mvp.domain.review.dto.ReviewResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MyPageResponse {
    private String userNickName;
    private String userEmail;
    private String userProfileUrl;
    private List<ReservationResponse> reservations;
    private List<ReviewResponse> reviews;
}
