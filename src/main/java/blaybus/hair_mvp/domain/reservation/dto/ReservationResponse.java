package blaybus.hair_mvp.domain.reservation.dto;

import blaybus.hair_mvp.domain.designer.entity.MeetingType;

import blaybus.hair_mvp.domain.payment.entity.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Builder
public class ReservationResponse {
   private UUID id;
   private LocalDateTime reservationAt;
   private String designerName;
   private String shopAddress;
   private MeetingType meetingType;
   private Integer price;
   private Status status;
   private String googleMeetLink;
   @JsonProperty("isCurrent")
   private boolean isCurrent;
}
