package blaybus.hair_mvp.auth.dto;

import java.util.Date;


public record RefreshTokenPayload(String email, Date issuedAt) {
}