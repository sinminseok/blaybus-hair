package blaybus.hair_mvp.domain.kakao_Payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "approved_payment")
public class ApprovedPayment {

    @Id
    @GeneratedValue
    private Long id;

    private String aid;  // 승인 ID
    private String tid;  // 결제 고유 번호
    private String cid;  // 가맹점 코드
    private String partner_order_id;  // 주문번호
    private String partner_user_id;  // 사용자 ID
//    private String paymentMethodType;  // 결제 수단

    private Amount amount;

    private LocalDateTime approved_at;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
