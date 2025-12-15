package com.remitro.gateway.security.jwt;

import static com.remitro.common.security.AuthenticationConstant.*;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.remitro.common.security.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtClaimsExtractor {

	public Claims extract(String token, SecretKey secretKey) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	public Role extractRole(Claims claims) {
		return Role.valueOf(claims.get(CLAIM_MEMBER_ROLE, String.class));
	}

	public Long extractMemberId(Claims claims) {
		return claims.get(CLAIM_MEMBER_ID, Long.class);
	}
}
