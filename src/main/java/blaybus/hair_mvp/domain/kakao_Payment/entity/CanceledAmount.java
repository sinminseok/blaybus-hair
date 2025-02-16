package blaybus.hair_mvp.domain.kakao_Payment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CanceledAmount {

    private int canceled_total;

    public void setTotal(int canceled_total) { this.canceled_total = canceled_total; }
}
