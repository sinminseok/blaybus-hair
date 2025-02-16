package blaybus.hair_mvp.domain.reservation.service;

import blaybus.hair_mvp.domain.designer.entity.Designer;
import blaybus.hair_mvp.domain.designer.repository.DesignerRepository;
import blaybus.hair_mvp.domain.reservation.dto.ReservationRequest;
import blaybus.hair_mvp.domain.reservation.dto.ReservationResponse;
import blaybus.hair_mvp.domain.reservation.entity.Reservation;
import blaybus.hair_mvp.domain.reservation.mapper.ReservationMapper;
import blaybus.hair_mvp.domain.reservation.repository.ReservationRepository;
import blaybus.hair_mvp.domain.user.entity.User;
import blaybus.hair_mvp.domain.user.repository.UserRepository;
import blaybus.hair_mvp.utils.OptionalUtil;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final DesignerRepository designerRepository;
    private final UserRepository userRepository;
    private final ReservationMapper reservationMapper;

    @Transactional
    public void save(final ReservationRequest request, final String userEmail) {
        Reservation reservation = reservationMapper.toEntity(request);
        Designer designer = OptionalUtil.getOrElseThrow(
                designerRepository.findById(UUID.fromString(request.getDesignerId())), "존재하지 않는 디자이너 ID 입니다.");
        User user = OptionalUtil.getOrElseThrow(userRepository.findByEmail(userEmail), "존재하지 않는 사용자 ID 입니다.");
        reservation.setDesigner(designer);
        reservation.setUser(user);
        reservationRepository.save(reservation);
        designer.addReservation(reservation);
        user.addReservation(reservation);
    }

    public List<ReservationResponse> findReservationsByUserId(UUID userId) {
        List<Reservation> reservations = reservationRepository.findByUser_Id(userId);
        // status
        return reservations.stream().map(
                reservation -> ReservationResponse.builder()
                        .id(reservation.getId())
                        .designerName(reservation.getDesigner().getName())
                        .shopAddress(reservation.getDesigner().getShopAddress())
                        .meetingType(reservation.getMeetingType())
                        .price(reservation.getPrice())
                        // status 추가 예정
                        .build()
        ).toList();
    }
}
