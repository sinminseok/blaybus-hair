package blaybus.hair_mvp.auth.dto;


import blaybus.hair_mvp.domain.user.entity.Role;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private final Role role;
    private final ResponseCookie accessTokenCookie;
    private final ResponseCookie refreshTokenCookie;


}