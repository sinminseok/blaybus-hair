package blaybus.hair_mvp.domain.user.controller;

import blaybus.hair_mvp.auth.SecurityContextHelper;
import blaybus.hair_mvp.auth.service.CookieService;
import blaybus.hair_mvp.auth.service.LoginService;
import blaybus.hair_mvp.aws.s3.S3FileRepository;
import blaybus.hair_mvp.aws.s3.entity.S3File;
import blaybus.hair_mvp.aws.s3.service.S3FileService;
import blaybus.hair_mvp.aws.s3.service.S3Service;
import blaybus.hair_mvp.domain.user.dto.UserSignupRequest;
import blaybus.hair_mvp.domain.user.entity.User;
import blaybus.hair_mvp.domain.user.service.OAuthService;
import blaybus.hair_mvp.domain.user.service.UserService;
import blaybus.hair_mvp.utils.SuccessResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/users")
public class UserController {

    private final UserService userService;
    private final SecurityContextHelper securityContextHelper;
    private final S3Service s3Service;
    private final S3FileService s3FileService;

    private final String PROFILE_IMAGE_PATH = "user/profileImage/";

    @PostMapping
    public ResponseEntity<?> signUp(@RequestBody final UserSignupRequest request) {
        userService.save(request);
        SuccessResponse response = new SuccessResponse(true, "회원가입 성공", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> findMyProfile() {
        String emailInToken = securityContextHelper.getEmailInToken();
        SuccessResponse response = new SuccessResponse(true, "사용자 정보 조회 성공", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @PutMapping("/profileImage")
    public ResponseEntity<?> updateProfileImage(@RequestParam("profileImage") MultipartFile profileImage) {
        String emailInToken = securityContextHelper.getEmailInToken();
        User user = userService.findByEmail(emailInToken)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
        // 기존 프로필 사진이 존재하면 삭제
        if (user.getProfileImage() != null) {
            s3Service.deleteFile(user.getFile().getFileName());
            s3FileService.delete(user.getFile());
        }
        UUID uuid = UUID.randomUUID();
        String imageUrl = s3Service.uploadImage(PROFILE_IMAGE_PATH, uuid, profileImage);
        S3File s3file = S3File.builder()
                .filePath(PROFILE_IMAGE_PATH)
                .fileUUID(uuid)
                .fileName(profileImage.getOriginalFilename())
                .fileURL(imageUrl)
                .build();
        s3FileService.save(s3file);
        user.updateProfileImage(s3file);

        SuccessResponse<String> response = new SuccessResponse<>(true, "프로필 이미지 등록 성공", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
