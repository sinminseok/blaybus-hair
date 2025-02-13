package blaybus.hair_mvp.auth.dto;


import blaybus.hair_mvp.domain.user.entity.Role;

import java.util.Date;


public record AccessTokenPayload(String email, Role roleEnum, Date issuedAt) {

}