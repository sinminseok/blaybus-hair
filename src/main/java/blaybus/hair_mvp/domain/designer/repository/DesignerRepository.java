package blaybus.hair_mvp.domain.designer.repository;

import blaybus.hair_mvp.domain.designer.entity.Designer;
import blaybus.hair_mvp.domain.designer.entity.MeetingType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignerRepository extends JpaRepository<Designer, UUID> {
    Optional<Designer> findByName(String name);
    Page<Designer> findByStyling(String styling, Pageable pageable);
    Page<Designer> findDesignerByMeetingTypeAndStyling(MeetingType meetingType, String Styling, Pageable pageable);
    Page<Designer> findByRegion(String region, Pageable pageable);
}
