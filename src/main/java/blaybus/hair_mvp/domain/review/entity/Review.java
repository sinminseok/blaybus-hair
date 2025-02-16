package blaybus.hair_mvp.domain.review.entity;

import blaybus.hair_mvp.domain.common.BaseTimeEntity;
import blaybus.hair_mvp.domain.designer.entity.Designer;
import blaybus.hair_mvp.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`review`")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
public class Review extends BaseTimeEntity {

    @Id
    @UuidGenerator
    @Column(name = "review_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "star_point", nullable = false, updatable = false)
    private Integer starPoint;

    @Column(name = "content", nullable = false, updatable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id", nullable = false)
    @Setter
    private Designer designer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Setter
    private User user;
}
