package blaybus.hair_mvp.domain.reservation.entity;

import blaybus.hair_mvp.domain.designer.entity.Designer;
import blaybus.hair_mvp.domain.designer.entity.MeetingType;
import blaybus.hair_mvp.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`reservation`")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
public class Reservation {

    @Id
    @UuidGenerator
    @Column(name = "reservation_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "meeting_type", nullable = false)
    private MeetingType meetingType;

    @Column(name = "reservation_at", nullable = false)
    private LocalDateTime reservationAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id", nullable = false)
    @Setter
    private Designer designer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Setter
    private User user;

    @Setter
    @Column(name = "google_meet_link", nullable = true)
    private String googleMeetLink;

}
