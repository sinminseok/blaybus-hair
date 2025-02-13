package blaybus.hair_mvp.auth.dto;

import java.util.Date;


public record RefreshTokenPayload(String tokenId, Date issuedAt) {
}