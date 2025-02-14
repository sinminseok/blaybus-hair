package blaybus.hair_mvp.domain.kakao_Payment.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Amount {
    private int total;
//    private int tax_free;
//    private int vat;
//    private int point;
//    private int discount;
//    private int green_depostit;

}
