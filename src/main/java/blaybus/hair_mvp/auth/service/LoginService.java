package blaybus.hair_mvp.auth.service;

import blaybus.hair_mvp.auth.dto.LoginResponse;
import blaybus.hair_mvp.auth.dto.AccessTokenPayload;
import blaybus.hair_mvp.auth.jwt.JwtService;
import blaybus.hair_mvp.auth.dto.RefreshTokenPayload;
import blaybus.hair_mvp.domain.user.entity.RefreshToken;
import blaybus.hair_mvp.domain.user.entity.User;
import blaybus.hair_mvp.domain.user.repository.RefreshTokenRepository;
import blaybus.hair_mvp.domain.user.repository.UserRepository;
import blaybus.hair_mvp.exception.ErrorResponseCode;
import blaybus.hair_mvp.exception.code.EmailExistExceptionCode;
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
    public LoginResponse login(String email){
        User user = getValidatedUser(email);
        RefreshToken updateRefreshToken = updateRefreshToken(user);
        AccessTokenPayload accessTokenPayload = new AccessTokenPayload(user.getEmail(), user.getRole(), new Date());
        String accessToken = jwtService.createAccessToken(accessTokenPayload);
        String refreshToken = jwtService.createRefreshToken(new RefreshTokenPayload(updateRefreshToken.getId().toString(), new Date()));
        ResponseCookie accessTokenCookie = cookieService.createAccessTokenCookie(accessToken, Duration.ofMillis(accessKeyExpirationInMs));
        return new LoginResponse(user.getRole(), accessTokenCookie, refreshToken);
    }

    private RefreshToken updateRefreshToken(User user){
        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);
        return refreshTokenRepository.save(RefreshToken.builder()
                .user(user)
                .expiredAt(LocalDateTime.now().plus(Duration.ofMillis(refreshKeyExpirationInMs)))
                .build());
    }

    private User getValidatedUser(String email){
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new EmailExistExceptionCode(ErrorResponseCode.NOT_FOUND, "존재하지 않는 이메일 입니다.");
        }
        return user;
    }
}
