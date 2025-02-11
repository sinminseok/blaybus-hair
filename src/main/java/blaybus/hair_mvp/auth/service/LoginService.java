package blaybus.hair_mvp.auth.service;

import blaybus.hair_mvp.auth.dto.LoginCommandDto;
import blaybus.hair_mvp.auth.dto.LoginResultDto;
import blaybus.hair_mvp.auth.jwt.AccessTokenPayload;
import blaybus.hair_mvp.auth.jwt.JwtService;
import blaybus.hair_mvp.auth.jwt.RefreshTokenPayload;
import blaybus.hair_mvp.domain.user.entity.RefreshToken;
import blaybus.hair_mvp.domain.user.entity.User;
import blaybus.hair_mvp.domain.user.repository.RefreshTokenRepository;
import blaybus.hair_mvp.domain.user.repository.UserRepository;
import blaybus.hair_mvp.exception.ErrorResponseCode;
import blaybus.hair_mvp.exception.code.EmailExistExceptionCode;
import blaybus.hair_mvp.exception.code.PasswordMatchExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieService cookieService;

    @Value("#{${jwt.access-key-expiration-s} * 1000}")
    private long accessKeyExpirationInMs;

    @Value("#{${jwt.refresh-key-expiration-s} * 1000}")
    private long refreshKeyExpirationInMs;

    @Transactional
    public LoginResultDto login(LoginCommandDto command){
        User user = getValidatedUser(command.getEmail(), command.getPassword());
        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);
        RefreshToken saved = refreshTokenRepository.save(RefreshToken.builder()
                        .user(user)
                        .expiredAt(LocalDateTime.now().plus(Duration.ofMillis(refreshKeyExpirationInMs)))
                .build());

        AccessTokenPayload accessTokenPayload = new AccessTokenPayload(user.getEmail(), user.getRole(), new Date());
        String accessToken = jwtService.createAccessToken(accessTokenPayload);
        String refreshToken = jwtService.createRefreshToken(new RefreshTokenPayload(saved.getId().toString(), new Date()));

        ResponseCookie accessTokenCookie = cookieService.createAccessTokenCookie(accessToken, Duration.ofMillis(accessKeyExpirationInMs));

        return new LoginResultDto(user.getRole(), accessTokenCookie, refreshToken);
    }

    private User getValidatedUser(String email, String password){
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new EmailExistExceptionCode(ErrorResponseCode.NOT_FOUND, "존재하지 않는 이메일 입니다.");
        }
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new PasswordMatchExceptionCode(ErrorResponseCode.NOT_MATCH_PASSWORD, "비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

}
