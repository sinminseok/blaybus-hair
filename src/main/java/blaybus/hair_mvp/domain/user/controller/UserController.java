package blaybus.hair_mvp.domain.user.controller;

import blaybus.hair_mvp.auth.SecurityContextHelper;
import blaybus.hair_mvp.auth.service.CookieService;
import blaybus.hair_mvp.auth.service.LoginService;
import blaybus.hair_mvp.domain.user.dto.UserSignupRequest;
import blaybus.hair_mvp.domain.user.service.OAuthService;
import blaybus.hair_mvp.domain.user.service.UserService;
import blaybus.hair_mvp.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/users")
public class UserController {

    private final UserService userService;
    private final SecurityContextHelper securityContextHelper;

    @PostMapping
    public ResponseEntity<?> signUp(@RequestBody final UserSignupRequest request){
        userService.save(request);
        SuccessResponse response = new SuccessResponse(true, "회원가입 성공", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> findMyProfile(){
        String emailInToken = securityContextHelper.getEmailInToken();
        SuccessResponse response = new SuccessResponse(true, "사용자 정보 조회 성공", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
