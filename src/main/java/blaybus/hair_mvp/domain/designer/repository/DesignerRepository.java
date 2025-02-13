package blaybus.hair_mvp.domain.designer.repository;

import blaybus.hair_mvp.domain.designer.entity.Designer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignerRepository extends JpaRepository<Designer, UUID> {
    @EntityGraph(attributePaths = "regions")
    Optional<Designer> findByName(String name);
}
