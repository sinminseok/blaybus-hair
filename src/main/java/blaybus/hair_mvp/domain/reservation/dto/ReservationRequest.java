package blaybus.hair_mvp.domain.reservation.dto;

import blaybus.hair_mvp.domain.designer.entity.MeetingType;
import blaybus.hair_mvp.domain.kakao_Payment.entity.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReservationRequest {
    private String designerId;
    private Integer price;
    private MeetingType meetingType;
    private LocalDateTime reservationAt;
    private Status paymentStatus;
}
