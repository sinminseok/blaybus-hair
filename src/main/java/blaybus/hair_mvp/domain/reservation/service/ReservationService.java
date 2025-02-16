package blaybus.hair_mvp.domain.reservation.service;

import blaybus.hair_mvp.domain.designer.entity.Designer;
import blaybus.hair_mvp.domain.designer.entity.MeetingType;
import blaybus.hair_mvp.domain.designer.repository.DesignerRepository;
import blaybus.hair_mvp.domain.reservation.dto.ReservationRequest;
import blaybus.hair_mvp.domain.reservation.dto.ReservationResponse;
import blaybus.hair_mvp.domain.reservation.entity.Reservation;
import blaybus.hair_mvp.domain.reservation.mapper.ReservationMapper;
import blaybus.hair_mvp.domain.reservation.repository.ReservationRepository;
import blaybus.hair_mvp.domain.user.entity.User;
import blaybus.hair_mvp.domain.user.repository.UserRepository;
import blaybus.hair_mvp.infra.google_calendar.GoogleMeetHelper;
import blaybus.hair_mvp.utils.OptionalUtil;
import com.google.api.client.util.DateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final DesignerRepository designerRepository;
    private final UserRepository userRepository;
    private final ReservationMapper reservationMapper;
    private final GoogleMeetHelper googleMeetHelper;


    @Transactional
    public ReservationResponse save(final ReservationRequest request, final String userEmail){
        Reservation reservation = reservationMapper.toEntity(request);
        Designer designer = OptionalUtil.getOrElseThrow(designerRepository.findById(UUID.fromString(request.getDesignerId())), "존재하지 않는 디자이너 ID 입니다.");
        User user = OptionalUtil.getOrElseThrow(userRepository.findByEmail(userEmail), "존재하지 않는 사용자 ID 입니다.");
        reservation.setDesigner(designer);
        reservation.setUser(user);
        reservationRepository.save(reservation);
        designer.addReservation(reservation);
        user.addReservation(reservation);

        if (request.getMeetingType().equals(MeetingType.MEETING)) {
            //todo 회의 후 논의
            //generateGoogleMeetLink(reservation, designer);
        }
        return reservationMapper.toResponse(reservation, designer);
    }

    private void generateGoogleMeetLink(Reservation reservation, Designer designer) throws IOException {
        LocalDateTime reservationTime = reservation.getReservationAt();
        Date date = Date.from(reservationTime.atZone(ZoneId.systemDefault()).toInstant());
        DateTime googleDateTime = new DateTime(date);
        // 1시간 추가
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(googleDateTime.getValue());
        calendar.add(Calendar.HOUR, 1);  // 1시간 추가
        DateTime endDateTime = new DateTime(calendar.getTimeInMillis());
        // Google Meet 링크 생성
        String meetLink = googleMeetHelper.createGoogleMeetLink(designer.getName(), "미용실 예약 정보", googleDateTime, endDateTime);
        reservation.setGoogleMeetLink(meetLink);
    }

}
