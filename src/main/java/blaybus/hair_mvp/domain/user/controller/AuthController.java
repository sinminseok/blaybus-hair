package blaybus.hair_mvp.domain.user.controller;

import blaybus.hair_mvp.auth.dto.LoginResponse;
import blaybus.hair_mvp.auth.service.LoginService;
import blaybus.hair_mvp.domain.user.dto.GoogleAuthRequest;
import blaybus.hair_mvp.domain.user.dto.UserSignupRequest;
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
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/auth")
public class AuthController {

    private final UserService userService;
    private final OAuthService oAuthService;
    private final LoginService loginService;

    @PostMapping("/login/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody final GoogleAuthRequest request) throws GeneralSecurityException, IOException {
        UserSignupRequest userSignupRequest = oAuthService.getGoogleProfile(request.getAuthorizationCode());
        if (userService.isExistUser(userSignupRequest.getEmail())) {
            return createLoginResponse(userSignupRequest.getEmail(), "구글 로그인 성공");
        }
        userService.save(userSignupRequest);
        return createLoginResponse(userSignupRequest.getEmail(), "구글 로그인 성공");
    }

    private ResponseEntity<?> createLoginResponse(String email, String message) {
        LoginResponse loginResponse = loginService.login(email);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, loginResponse.getAccessTokenCookie().toString())
                .header(HttpHeaders.SET_COOKIE, loginResponse.getRefreshTokenCookie().toString())
                .body(new SuccessResponse(true, message, null));
    }
}

