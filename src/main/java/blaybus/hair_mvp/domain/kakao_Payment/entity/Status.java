package blaybus.hair_mvp.domain.kakao_Payment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public enum Status {
    READY("결제 대기"),
    CANCEL_PAYMENT("결제 취소"),
    SUCCESS_PAYMENT("결제 완료"),
    FAIL_PAYMENT("결제 취소"),
    DEPOSIT_WAITING("입금 확인중");

    private final String description;
}
