package blaybus.hair_mvp.domain.reservation.service;

import blaybus.hair_mvp.domain.designer.entity.Designer;
import blaybus.hair_mvp.domain.designer.repository.DesignerRepository;
import blaybus.hair_mvp.domain.reservation.dto.ReservationRequest;
import blaybus.hair_mvp.domain.reservation.entity.Reservation;
import blaybus.hair_mvp.domain.reservation.mapper.ReservationMapper;
import blaybus.hair_mvp.domain.reservation.repository.ReservationRepository;
import blaybus.hair_mvp.domain.user.entity.User;
import blaybus.hair_mvp.domain.user.repository.UserRepository;
import blaybus.hair_mvp.utils.OptionalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final DesignerRepository designerRepository;
    private final UserRepository userRepository;
    private final ReservationMapper reservationMapper;

    public void save(final ReservationRequest request, final String userEmail){
        Reservation reservation = reservationMapper.toEntity(request);
        Designer designer = OptionalUtil.getOrElseThrow(designerRepository.findById(UUID.fromString(request.getDesignerId())), "존재하지 않는 디자이너 ID 입니다.");
        designer.addReservation(reservation);
        User user = OptionalUtil.getOrElseThrow(userRepository.findByEmail(userEmail), "존재하지 않는 사용자 ID 입니다.");
        user.addReservation(reservation);
    }
}
