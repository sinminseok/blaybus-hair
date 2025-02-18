package blaybus.hair_mvp.domain.payment.entity;

import blaybus.hair_mvp.domain.payment.dto.KakaoApproveResponse;
import blaybus.hair_mvp.domain.payment.dto.KakaoCancelResponse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tid; // 결제 고유번호

    private String aid; // 요청 고유번호

    private String cid; // 가맹점 코드

    private String orderId;

    private String userId;

    private String item_name;

    @Setter
    @Enumerated(EnumType.STRING)
    private Status status;

    @Embedded
    private Amount amount;

    @Embedded
    private CanceledAmount canceled_amount;

    private LocalDateTime created_at;

    private LocalDateTime approved_at;

    private LocalDateTime canceled_at;

    // 결제 승인 메서드
    public void approvePayment(KakaoApproveResponse kakaoApproveResponse) {
        this.aid = kakaoApproveResponse.getAid();
        this.approved_at = LocalDateTime.parse(
                kakaoApproveResponse.getApproved_at(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        );
        this.status = Status.SUCCESS_PAYMENT;
    }
    // 결제 취소 메서드
    public void cancelPayment(KakaoCancelResponse kakaoCancelResponse) {
        if (this.canceled_amount == null) {
            this.canceled_amount = new CanceledAmount(); //  Null 방지
        }
        this.canceled_amount.setTotal(this.amount.getTotal()); // 일단 결제금액과 취소금액 같게함
        this.canceled_at = LocalDateTime.parse(
                kakaoCancelResponse.getCanceled_at(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        ); // 취소 시간 업데이트
        this.status = Status.CANCEL_PAYMENT; // 상태 변경
    }



}
