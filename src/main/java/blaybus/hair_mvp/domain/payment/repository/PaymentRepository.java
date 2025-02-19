package blaybus.hair_mvp.domain.payment.repository;


import blaybus.hair_mvp.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTid(String tid);

    Optional<Payment> findByOrderId(UUID orderId);

    Optional<Payment> findByTidAndCid(String tid, String cid);

//    Optional<Payment> findByOrderIdAndStatus(String orderId, PaymentStatus paymentStatus);


}
