package blaybus.hair_mvp.domain.reservation.dto;

import blaybus.hair_mvp.domain.designer.entity.MeetingType;
import blaybus.hair_mvp.domain.payment.entity.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReservationCreateResponse {
    private LocalDateTime reservationAt;
    private MeetingType meetingType;
    private String designerName;
    private String shopAddress;
    private PaymentStatus paymentStatus;
}
