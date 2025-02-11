package blaybus.hair_mvp.auth.dto;

import java.util.UUID;


import blaybus.hair_mvp.domain.user.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class LoginResponseDto {
    private final String email;
    private final Role role;
    private final UUID userId;


    @Builder
    public LoginResponseDto(String email, Role role, UUID userId) {
        this.email = email;
        this.role = role;
        this.userId = userId;
    }

}