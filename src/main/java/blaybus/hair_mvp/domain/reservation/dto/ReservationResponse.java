package blaybus.hair_mvp.domain.reservation.dto;

import blaybus.hair_mvp.domain.designer.entity.MeetingType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Builder
public class ReservationResponse {
   private UUID id;
   private String designerName;
   private String shopAddress;
   private MeetingType meetingType;
   private Integer price;
   private Status status;
}
