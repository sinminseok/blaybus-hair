package blaybus.hair_mvp.domain.kakao_Payment.dto;

import blaybus.hair_mvp.domain.kakao_Payment.entity.Amount;
import blaybus.hair_mvp.domain.kakao_Payment.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private String tid;
    private String cid;
    private String status;
    private String partner_order_id;
    private String partner_user_id;
    private Amount amount;
    private String item_name;
    private String approved_at;
    private String canceled_at;

}
