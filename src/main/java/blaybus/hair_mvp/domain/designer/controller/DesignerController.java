package blaybus.hair_mvp.domain.designer.controller;

import blaybus.hair_mvp.auth.SecurityContextHelper;
import blaybus.hair_mvp.domain.designer.dto.DesignerResponse;
import blaybus.hair_mvp.domain.designer.dto.TimeSlot;
import blaybus.hair_mvp.domain.designer.service.ScheduleService;
import blaybus.hair_mvp.domain.user.dto.UserSurveyRequest;
import blaybus.hair_mvp.domain.designer.entity.Designer;
import blaybus.hair_mvp.domain.designer.entity.MeetingType;
import blaybus.hair_mvp.domain.designer.service.DesignerService;
import blaybus.hair_mvp.domain.user.entity.User;
import blaybus.hair_mvp.domain.user.repository.UserRepository;
import blaybus.hair_mvp.domain.user.service.UserService;
import blaybus.hair_mvp.utils.SuccessResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/designers")
public class DesignerController {

    private final DesignerService designerService;
    private final ScheduleService scheduleService;
    private final UserService userService;
    private final SecurityContextHelper securityContextHelper;

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
            @RequestParam String styling,
            @RequestBody UserSurveyRequest userSurveyRequest
            ){
        // 유저 선호 정보 업데이트
        String email = securityContextHelper.getEmailInToken();
        userService.updateUserSurvey(email, userSurveyRequest, styling);

        List<DesignerResponse> designers = designerService.findDesigner(page, size, meetingType, styling);
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
        DesignerResponse designer = designerService.findDesignerById(designerId);
        SuccessResponse<DesignerResponse> response = new SuccessResponse<>(true, "디자이너 상세 조회 성공", designer);
        return ResponseEntity.ok(response);
    }

    /**
     * 디자이너 스케줄 조회
     */
    @GetMapping("/{designerId}/schedule")
    public ResponseEntity<?> getDesignerSchedule(@PathVariable("designerId") UUID designerId) {
        Map<LocalDate, List<TimeSlot>> designerSchedule = scheduleService.getDesignerSchedule(designerId);
        SuccessResponse<Map<LocalDate, List<TimeSlot>>> response = new SuccessResponse<>(true, "디자이너 스케줄 조회 성공", designerSchedule);
        return ResponseEntity.ok(response);
    }
}
