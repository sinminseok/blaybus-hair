package blaybus.hair_mvp.auth.service;

import blaybus.hair_mvp.constants.JwtMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Value("${jwt.access-key-expiration-s}")
    private long accessKeyExpirationInS;

    @Value("${jwt.refresh-key-expiration-s}")
    private long refreshKeyExpirationInS;

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .maxAge(refreshKeyExpirationInS)
                .sameSite("Strict")
                .path("/")
                .build();
    }

    public ResponseCookie createAccessTokenCookie(String accessToken) {
        return ResponseCookie.from(JwtMetadata.ACCESS_TOKEN, accessToken)
                .httpOnly(true)
                .secure(true)
                .maxAge(accessKeyExpirationInS)
                .path("/")
                .sameSite("Strict")
                .build();
    }

}
