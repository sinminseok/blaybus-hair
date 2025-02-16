package blaybus.hair_mvp.domain.reservation.repository;

import blaybus.hair_mvp.domain.reservation.entity.Reservation;
import jakarta.persistence.Entity;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    @EntityGraph(attributePaths = {"designer"})
    List<Reservation> findByUser_Id(UUID userId);
}
