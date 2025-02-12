package blaybus.hair_mvp.domain.user.controller;

import blaybus.hair_mvp.auth.service.CookieService;
import blaybus.hair_mvp.auth.service.LoginService;
import blaybus.hair_mvp.domain.user.dto.UserSignupRequest;
import blaybus.hair_mvp.domain.user.service.OAuthService;
import blaybus.hair_mvp.domain.user.service.UserService;
import blaybus.hair_mvp.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> signUp(@RequestBody final UserSignupRequest request){
        userService.save(request);
        SuccessResponse response = new SuccessResponse(true, "회원가입 성공", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
