package blaybus.hair_mvp.domain.reservation.repository;

import blaybus.hair_mvp.domain.reservation.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID>, CustomReservationRepository {

    @Query("SELECT r.reservationAt FROM Reservation r WHERE r.designer.id = :designerId AND r.reservationAt BETWEEN :startDate AND :endDate")
    List<LocalDateTime> findByDesignerIdAndDateRange(
            @Param("designerId") UUID designerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    List<Reservation> findByUserId(UUID userId);
}
