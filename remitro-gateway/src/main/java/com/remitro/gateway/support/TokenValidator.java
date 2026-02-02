package com.remitro.gateway.support;

import org.springframework.stereotype.Component;

import com.remitro.gateway.error.GatewayErrorCode;
import com.remitro.gateway.error.GatewayException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenValidator {

	private static final String CLAIM_ROLE = "role";

	private final SigningKeyProvider signingKeyProvider;

	public boolean isValid(String token) {
		if (token == null || token.isBlank()) {
			return false;
		}

		try {
			parseClaims(token);
			return true;

		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public Long extractMemberId(String token) {
		final Claims claims = parseClaims(token);
		return Long.parseLong(claims.getSubject());
	}

	public String extractRole(String token) {
		final Claims claims = parseClaims(token);
		final String role = claims.get(CLAIM_ROLE, String.class);

		if (role == null) {
			throw new GatewayException(GatewayErrorCode.UNAUTHORIZED);
		}

		return role;
	}

	private Claims parseClaims(String token) {
		return Jwts.parser()
			.verifyWith(signingKeyProvider.getSecretKey())
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
}
