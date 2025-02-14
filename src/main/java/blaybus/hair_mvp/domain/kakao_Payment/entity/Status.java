package blaybus.hair_mvp.domain.kakao_Payment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public enum Status {
    READY("결제 대기"),
    CANCEL("결제 완료"),
    SUCCESS("결제 취소");

    private final String description;
}
