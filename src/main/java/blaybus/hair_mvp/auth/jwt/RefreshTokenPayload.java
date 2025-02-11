package blaybus.hair_mvp.auth.jwt;

import java.util.Date;

public record RefreshTokenPayload(String tokenId, Date issuedAt) {
}