package blaybus.hair_mvp.domain.kakao_Payment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;
    private String userId;
    private String tid;

    @Setter
    @Enumerated(EnumType.STRING)
    private Status status;

    private int amount;
}
