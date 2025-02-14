package blaybus.hair_mvp.domain.kakao_Payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Embedded
    private Amount amount;

    private LocalDateTime created_at;

    @OneToOne(mappedBy = "payment",cascade = CascadeType.ALL)
    private ApprovedPayment approvedPayment;


}
