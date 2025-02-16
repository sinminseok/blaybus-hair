package blaybus.hair_mvp.domain.designer.repository;

import blaybus.hair_mvp.domain.designer.entity.Designer;
import blaybus.hair_mvp.domain.designer.entity.MeetingType;
import java.util.List;

public interface DesignerRepositoryCustom {
    List<Designer> searchDesigners(
            Integer page, Integer size, MeetingType meetingType, String styling,
            String region, String minPrice, String maxPrice);

    public List<Designer> findDesignerByConditions(int page, int size, String styling);
    List<Designer> findDesignerByConditions(int page, int size, MeetingType meetingType, String styling);
    }
