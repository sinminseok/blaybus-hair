package blaybus.hair_mvp.domain.kakao_Payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KakaoDepositResponse {
    private String link;
    private String created_at;
}
