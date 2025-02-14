package blaybus.hair_mvp.domain.kakao_Payment.dto;


import blaybus.hair_mvp.domain.kakao_Payment.entity.Amount;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private Amount amount;

}
