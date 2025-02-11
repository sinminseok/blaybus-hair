package blaybus.hair_mvp.auth.dto;


import blaybus.hair_mvp.domain.user.entity.Role;
import org.springframework.http.ResponseCookie;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class LoginResultDto {
    private Role role;
    private String refreshToken;
    private ResponseCookie accessTokenCookie;

    @Builder
    public LoginResultDto(Role role, ResponseCookie responseCookie, String refreshToken) {
        this.role = role;
        this.refreshToken = refreshToken;
        this.accessTokenCookie = responseCookie;
    }

}