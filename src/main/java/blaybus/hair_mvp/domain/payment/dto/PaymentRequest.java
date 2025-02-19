package blaybus.hair_mvp.domain.payment.dto;


import blaybus.hair_mvp.domain.payment.entity.Amount;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PaymentRequest {
    private UUID orderId;
    private UUID userId;
    private String itemName;
    @NotBlank
    private Amount amount;

}
