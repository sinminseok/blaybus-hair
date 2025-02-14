package blaybus.hair_mvp.domain.kakao_Payment.dto;



import blaybus.hair_mvp.domain.kakao_Payment.entity.Amount;
import blaybus.hair_mvp.domain.kakao_Payment.entity.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoApproveResponse {
    private String aid; // 요청 고유 번호
    private String tid; // 결제 고유 번호
    private String cid; // 가맹점 코드
    private String partner_order_id; // 가맹점 주문 번호
    private String partner_user_id; // 가맹점 회원 id
    private String item_name;
    private int quantity;
    private Amount amount;
    private String created_at;
    private String approved_at;

    // 날짜 타입을 String 으로 해야 반환할 떄 null 값이 안생김


}
