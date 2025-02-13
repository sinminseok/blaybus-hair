package blaybus.hair_mvp.domain.kakao_Payment.repository;

import blaybus.hair_mvp.domain.kakao_Payment.entity.ApprovedPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovedPaymentRepository extends JpaRepository<ApprovedPayment, Long> {
}
