package blaybus.hair_mvp.domain.payment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    READY("결제 대기"),
    CANCEL_PAYMENT("결제 취소"),
    SUCCESS_PAYMENT("결제 완료"),
    DEPOSIT_WAITING("입금 확인중");

    private final String description;
}
