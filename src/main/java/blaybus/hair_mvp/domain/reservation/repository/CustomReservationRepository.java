package blaybus.hair_mvp.domain.reservation.repository;

import blaybus.hair_mvp.domain.reservation.entity.Reservation;

import java.util.List;
import java.util.UUID;

public interface CustomReservationRepository {
    List<Reservation> findCancelReservationByUserId(final UUID userId);

    List<Reservation> findCurrentReservationByUserId(final UUID userId);

    List<Reservation> findNotCancelReservationByUserId(final UUID userId);
}
