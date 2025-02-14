package blaybus.hair_mvp.domain.designer.controller;

import blaybus.hair_mvp.domain.designer.dto.DesignerResponse;
import blaybus.hair_mvp.domain.designer.entity.Designer;
import blaybus.hair_mvp.domain.designer.entity.MeetingType;
import blaybus.hair_mvp.domain.designer.repository.DesignerRepositoryImpl;
import blaybus.hair_mvp.domain.designer.service.DesignerService;
import blaybus.hair_mvp.utils.SuccessResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/designer")
public class DesignerController {

    private final DesignerService designerService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllDesigner(
            @RequestParam int page,
            @RequestParam int size
            ){
        List<Designer> designers = designerService.findAllDesigner(page, size);
        SuccessResponse<List<Designer>> response = new SuccessResponse<>(true, "모든 디자이너 조회 성공", designers);
        return ResponseEntity.ok(response);
    }

    /**
     * 사용자 선택 후 첫 화면 조회
     */
    @GetMapping("/")
    public ResponseEntity<?> getDesigner(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam MeetingType meetingType,
            @RequestParam String styling
            ){
        // 대면, 비대면 둘 다 선택한 경우 카테고리에 맞는 디자이너 조회
        if (meetingType == MeetingType.BOTH) {
            List<DesignerResponse> designers = designerService.findDesignerBySty(page, size, styling);
            SuccessResponse<List<DesignerResponse>> response = new SuccessResponse<>(true, "대면/비대면 디자이너 조회 성공", designers);
            return ResponseEntity.ok(response);
        }
        // 대면, 비대면 중 하나만 선택한 경우 해당하는 디자이너 조회
        List<DesignerResponse> designers = designerService.findDesignerByMtAndSty(page, size, meetingType, styling);
        SuccessResponse<List<DesignerResponse>> response = new SuccessResponse<>(true, meetingType + "디자이너 조회 성공", designers);
        return ResponseEntity.ok(response);
    }

    /**
     * 카테고리별 디자이너 조회
     */
    @GetMapping("/filter")
    public ResponseEntity<?> getDesignerByFilter(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam MeetingType meetingType,
            @RequestParam(required = false) String styling,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) String maxPrice){
        List<DesignerResponse> designers = designerService.findDesignerByFilters(page, size, meetingType, styling, region, minPrice, maxPrice);
        SuccessResponse<List<DesignerResponse>> response = new SuccessResponse<>(true, "디자이너 필터링 조회 성공", designers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{designerId}")
    public ResponseEntity<?> getDesignerDetail(@PathVariable UUID designerId) {
        Designer designer = designerService.findDesignerById(designerId);
        SuccessResponse<Designer> response = new SuccessResponse<>(true, "디자이너 상세 조회 성공", designer);
        return ResponseEntity.ok(response);
    }
}
