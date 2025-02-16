package blaybus.hair_mvp.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSignupRequest {
    private String email;
    private String name;
}
