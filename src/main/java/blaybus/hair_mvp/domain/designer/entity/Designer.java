package blaybus.hair_mvp.domain.designer.entity;

import blaybus.hair_mvp.domain.reservation.entity.Reservation;
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

    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "styling", nullable = false)
    private String styling;

    @Column(name = "f2f_consult_fee", nullable = false)
    private int offlineConsultFee;

    @Column(name = "online_consult_fee", nullable = false)
    private int onlineConsultFee;

    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;

    @Column(name = "bio", length = 30)
    private String bio;

    @OneToMany(mappedBy = "designer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    @Builder
    public Designer(String name, String shopAddress, String region, String styling, int offlineConsultFee, int onlineConsultFee, MeetingType meetingType, String bio) {
        this.name = name;
        this.shopAddress = shopAddress;
        this.region = region;
        this.styling = styling;
        this.offlineConsultFee = offlineConsultFee;
        this.onlineConsultFee = onlineConsultFee;
        this.meetingType = meetingType;
        this.bio = bio;
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setDesigner(this);
    }
}
