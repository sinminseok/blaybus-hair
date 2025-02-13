package blaybus.hair_mvp.domain.user.controller;

import blaybus.hair_mvp.auth.dto.LoginResponse;
import blaybus.hair_mvp.auth.service.LoginService;
import blaybus.hair_mvp.domain.user.dto.GoogleAuthRequest;
import blaybus.hair_mvp.domain.user.service.OAuthService;
import blaybus.hair_mvp.domain.user.service.UserService;
import blaybus.hair_mvp.utils.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/auth")
public class AuthController {

    private final UserService userService;
    private final OAuthService oAuthService;
    private final LoginService loginService;

    @PostMapping("/login/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody final GoogleAuthRequest request, HttpServletResponse httpServletResponse) throws GeneralSecurityException, IOException {
        String email = oAuthService.getGoogleEmail(request);
        //이메일이 존재할때
        if(userService.isExistUser(email)){
            LoginResponse loginResponse = loginService.login(email);
            httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, loginResponse.getAccessTokenCookie().getValue());
            httpServletResponse.addHeader(HttpHeaders.AUTHORIZATION, loginResponse.getRefreshToken());
            SuccessResponse response = new SuccessResponse(true,  "구글 로그인 성공", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        //존재하지 않을때
        SuccessResponse response = new SuccessResponse(false, "아직 가입되지 않은 구글 계정입니다.", email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
