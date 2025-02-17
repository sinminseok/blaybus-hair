package blaybus.hair_mvp.domain.user.entity;

import blaybus.hair_mvp.aws.s3.entity.S3File;
import blaybus.hair_mvp.domain.common.BaseTimeEntity;
import blaybus.hair_mvp.domain.designer.dto.UserPreferencesRequest;
import blaybus.hair_mvp.domain.designer.entity.MeetingType;
import blaybus.hair_mvp.domain.reservation.entity.Reservation;
import blaybus.hair_mvp.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
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

    @Column(name = "face_shape")
    @Setter
    private String faceShape;

    @Column(name = "hair_condition")
    @Setter
    private String hairCondition;

    @Column(name = "personal_color")
    @Setter
    private String personalColor;

    @Column(name = "styling")
    @Setter
    private String styling;

    @Column(name = "profile_image")
    @Setter
    private String profileImage;

    @Setter
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private S3File file;

    @Enumerated(EnumType.STRING)
    @Setter
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setUser(this);
    }

    public void addReview(Review review) {
        this.reviews.add(review);
        review.setUser(this);
    }
    public void updateProfileImage(S3File file) {
        this.file = file;
        this.profileImage = file.getFileURL();
    }
    public void updatePreference(UserPreferencesRequest request, String styling) {
        this.faceShape = request.getFaceShape();
        this.hairCondition = request.getHairCondition();
        this.personalColor = request.getPersonalColor();
        this.styling = styling;
    }
}
