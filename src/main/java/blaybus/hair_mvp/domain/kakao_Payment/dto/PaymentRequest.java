package blaybus.hair_mvp.domain.kakao_Payment.dto;


import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PaymentRequest {
    private String orderId;
    private String userId;
    private String itemName;
    private int amount;

}
