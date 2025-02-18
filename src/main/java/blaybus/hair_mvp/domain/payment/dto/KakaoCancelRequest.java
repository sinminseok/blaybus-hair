package blaybus.hair_mvp.domain.payment.dto;

import blaybus.hair_mvp.domain.payment.entity.CanceledAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoCancelRequest {
    private String cid;
    private String tid;
    private CanceledAmount cancel_amount;

}
