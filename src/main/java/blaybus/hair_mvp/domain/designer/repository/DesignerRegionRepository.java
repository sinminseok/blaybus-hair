package blaybus.hair_mvp.domain.designer.repository;

import blaybus.hair_mvp.domain.designer.entity.DesignerRegion;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignerRegionRepository extends JpaRepository<DesignerRegion, UUID> {
}
