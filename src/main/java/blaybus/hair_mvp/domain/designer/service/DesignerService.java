package blaybus.hair_mvp.domain.designer.service;

import blaybus.hair_mvp.domain.designer.dto.DesignerResponse;
import blaybus.hair_mvp.domain.designer.entity.Designer;
import blaybus.hair_mvp.domain.designer.entity.MeetingType;
import blaybus.hair_mvp.domain.designer.mapper.DesignerMapper;
import blaybus.hair_mvp.domain.designer.repository.DesignerRepository;
import blaybus.hair_mvp.domain.designer.repository.DesignerRepositoryImpl;
import blaybus.hair_mvp.utils.OptionalUtil;
import java.util.List;
import java.util.UUID;
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

    public Designer findDesignerById(UUID id) {
        return OptionalUtil.getOrElseThrow(designerRepository.findById(id), "존재하지 않는 디자이너 ID 입니다.");
    }

    public List<DesignerResponse> findDesignerBySty(int page, int size, String styling) {
        List<Designer> designers = designerRepositoryImpl.findDesignerByConditions(page, size, styling);
        return designerMapper.toResponseList(designers);
    }

    public List<DesignerResponse> findDesignerByMtAndSty(int page, int size, MeetingType meetingType, String styling) {
        List<Designer> designers = designerRepositoryImpl.findDesignerByConditions(page, size, meetingType,
                styling);
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
