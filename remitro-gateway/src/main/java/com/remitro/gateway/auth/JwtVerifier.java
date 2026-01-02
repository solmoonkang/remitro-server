package com.remitro.gateway.auth;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.remitro.common.security.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtVerifier {

	private final SecretKey secretKey;

	public AuthContext verify(String token) {
		Claims claims = Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();

		return new AuthContext(
			claims.get("memberId", Long.class),
			Role.valueOf(claims.get("role", String.class))
		);
	}
}
