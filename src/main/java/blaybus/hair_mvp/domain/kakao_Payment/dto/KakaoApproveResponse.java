package blaybus.hair_mvp.domain.kakao_Payment.dto;


import blaybus.hair_mvp.domain.kakao_Payment.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
public class KakaoApproveResponse {
    private String aid; // 요청 고유 번호
    private String tid; // 결제 고유 번호
    private String cid; // 가맹점 코드
    private String partner_order_id; // 가맹점 주문 번호
    private String partner_user_id; // 가맹점 회원 id
    private int amount;
    private String item_name;
    private Date created_at;
    private Date approved_at;
    private String payload;
    private Status status;
}
