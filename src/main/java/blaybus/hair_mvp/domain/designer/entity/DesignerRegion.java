package blaybus.hair_mvp.domain.designer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "designer_region")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DesignerRegion {
    @Id
    @UuidGenerator
    @Column(name = "region_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id", nullable = false)
    Designer designer;

    public DesignerRegion(String name, Designer designer) {
        this.name = name;
        this.designer = designer;
    }
}
