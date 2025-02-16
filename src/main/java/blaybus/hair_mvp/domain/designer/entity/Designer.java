package blaybus.hair_mvp.domain.designer.entity;

import blaybus.hair_mvp.domain.reservation.entity.Reservation;
import blaybus.hair_mvp.domain.review.entity.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "designer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Designer {
    @Id
    @UuidGenerator
    @Column(name = "designer_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "shop_address", nullable = false)
    private String shopAddress;

    @OneToMany(mappedBy = "designer", cascade = CascadeType.ALL)
    private List<DesignerRegion> regions;

    @OneToMany(mappedBy = "designer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "designer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "f2f_consult_fee", nullable = false)
    private int f2fConsultFee;

    @Column(name = "online_consult_fee", nullable = false)
    private int onlineConsultFee;

    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;

    @Column(name = "bio", length = 30)
    private String bio;

    @Builder
    public Designer(String name, String shopAddress, String category, int f2fConsultFee, int onlineConsultFee, MeetingType meetingType, String bio) {
        this.name = name;
        this.shopAddress = shopAddress;
        this.category = category;
        this.f2fConsultFee = f2fConsultFee;
        this.onlineConsultFee = onlineConsultFee;
        this.meetingType = meetingType;
        this.bio = bio;
    }

    public void addRegion(DesignerRegion region) {
        this.regions.add(region);
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setDesigner(this);
    }

    public void addReview(Review review){
        this.reviews.add(review);
        review.setDesigner(this);
    }
}
