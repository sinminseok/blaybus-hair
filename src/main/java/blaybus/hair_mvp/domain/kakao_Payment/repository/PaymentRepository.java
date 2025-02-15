package blaybus.hair_mvp.domain.kakao_Payment.repository;



import blaybus.hair_mvp.domain.kakao_Payment.entity.Payment;
import blaybus.hair_mvp.domain.kakao_Payment.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

Optional<Payment> findByTid(String tid);

Optional<Payment> findByOrderId(String orderId);

    Optional<Payment> findByTidAndCid(String tid, String cid);

Optional<Payment> findByOrderIdAndStatus(String orderId, Status status);


}
