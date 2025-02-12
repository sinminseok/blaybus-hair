package blaybus.hair_mvp.domain.kakao_Payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    private String orderId;
    private String userId;
    private String itemName;
    private int amount;

}
