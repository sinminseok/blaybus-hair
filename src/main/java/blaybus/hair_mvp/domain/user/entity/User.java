package blaybus.hair_mvp.domain.user.entity;

import blaybus.hair_mvp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`user`")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
public class User extends BaseTimeEntity {

    @Id
    @UuidGenerator
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    @Setter
    private String name;

    @Setter
    @Column(name = "profile_url", nullable = true)
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    @Setter
    private Role role;
}
