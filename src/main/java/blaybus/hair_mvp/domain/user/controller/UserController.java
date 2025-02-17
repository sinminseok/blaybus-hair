package blaybus.hair_mvp.domain.user.controller;

import blaybus.hair_mvp.auth.SecurityContextHelper;
import blaybus.hair_mvp.auth.service.CookieService;
import blaybus.hair_mvp.auth.service.LoginService;
import blaybus.hair_mvp.aws.s3.S3FileRepository;
import blaybus.hair_mvp.aws.s3.entity.S3File;
import blaybus.hair_mvp.aws.s3.service.S3FileService;
import blaybus.hair_mvp.aws.s3.service.S3Service;
import blaybus.hair_mvp.domain.user.dto.MyPageResponse;
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

    @GetMapping("/my_page")
    public ResponseEntity<?> findByPage(){
        String emailInToken = securityContextHelper.getEmailInToken();
        MyPageResponse myPageInformation = userService.findMyPageInformation(emailInToken);
        SuccessResponse response = new SuccessResponse(true, "마이페이지 조회 성공", myPageInformation);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
