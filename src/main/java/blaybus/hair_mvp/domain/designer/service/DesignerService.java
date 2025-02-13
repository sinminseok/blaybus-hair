package blaybus.hair_mvp.domain.designer.service;

import blaybus.hair_mvp.domain.designer.dto.DesignerResponse;
import blaybus.hair_mvp.domain.designer.entity.Designer;
import blaybus.hair_mvp.domain.designer.entity.MeetingType;
import blaybus.hair_mvp.domain.designer.mapper.DesignerMapper;
import blaybus.hair_mvp.domain.designer.repository.DesignerRepository;
import blaybus.hair_mvp.domain.designer.repository.DesignerRepositoryImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DesignerService {

    private final DesignerRepository designerRepository;
    private final DesignerRepositoryImpl designerRepositoryImpl;
    private final DesignerMapper designerMapper;

    public List<Designer> findAllDesigner(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return designerRepository.findAll(pageable).getContent();
    }

    public List<DesignerResponse> findDesignerBySty(int page, int size, String styling) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Order.asc("meetingType"),
                Sort.Order.asc("name")
        ));
        List<Designer> designers = designerRepository.findByStyling(styling, pageable).getContent();
        return designerMapper.toResponseList(designers);
    }

    public List<DesignerResponse> findDesignerByMtAndSty(int page, int size, MeetingType meetingType, String styling) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        List<Designer> designers = designerRepository.findDesignerByMeetingTypeAndStyling(meetingType, styling,
                pageable).getContent();
        return designerMapper.toResponseList(designers);
    }

    /**
     * 디자이너 필터링 조회
     */
    public List<DesignerResponse> findDesignerByFilters(int page, int size, MeetingType meetingType,
            String styling, String region, String minPrice, String maxPrice) {
        List<Designer> designers = designerRepositoryImpl.searchDesigners(page, size, meetingType, styling, region,
                minPrice, maxPrice);
        return designerMapper.toResponseList(designers);
    }
}
