package blaybus.hair_mvp.domain.kakao_Payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoReadyResponse {
    private String tid;
    private String next_redirect_mobile_url;
    private String next_redirect_pc_url;
    private Date created_at;
}
