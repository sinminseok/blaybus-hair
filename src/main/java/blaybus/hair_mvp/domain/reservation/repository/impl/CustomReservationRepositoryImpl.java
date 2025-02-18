package blaybus.hair_mvp.domain.reservation.repository.impl;

import blaybus.hair_mvp.domain.payment.entity.Status;
import blaybus.hair_mvp.domain.reservation.entity.QReservation;
import blaybus.hair_mvp.domain.reservation.entity.Reservation;
import blaybus.hair_mvp.domain.reservation.repository.CustomReservationRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomReservationRepositoryImpl implements CustomReservationRepository {

    private final JPAQueryFactory query;
    private QReservation qReservation = QReservation.reservation;

    @Override
    public List<Reservation> findCancelReservationByUserId(UUID userId) {
        return query.selectFrom(qReservation)
                .where(qReservation.user.id.eq(userId)
                        .and(qReservation.paymentStatus.eq(Status.CANCEL_PAYMENT)))
                .fetch();
    }

    @Override
    public List<Reservation> findCurrentReservationByUserId(UUID userId) {
        // 현재 시간
        LocalDateTime now = LocalDateTime.now();

        return query.selectFrom(qReservation)
                .where(qReservation.user.id.eq(userId)
                        .and(qReservation.paymentStatus.eq(Status.SUCCESS_PAYMENT))
                        .and(qReservation.reservationAt.gt(now)))
                .fetch();
    }
}
