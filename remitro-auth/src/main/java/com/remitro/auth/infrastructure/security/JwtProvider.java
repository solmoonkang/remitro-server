package com.remitro.auth.infrastructure.security;

import static com.remitro.common.security.AuthenticationConstant.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.UnauthorizedException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.common.security.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

	private final TokenConfig tokenConfig;
	private SecretKey secretKey;

	@PostConstruct
	public void init() {
		this.secretKey = Keys.hmacShaKeyFor(tokenConfig.getSecret().getBytes(StandardCharsets.UTF_8));
	}

	public String generateAccessToken(Long id, String email, String nickname, Role role) {
		return Jwts.builder()
			.issuer(tokenConfig.getIssuer())
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + tokenConfig.getAccessTokenExpiration()))
			.claim(CLAIM_MEMBER_ID, id)
			.claim(CLAIM_MEMBER_EMAIL, email)
			.claim(CLAIM_MEMBER_NICKNAME, nickname)
			.claim(CLAIM_MEMBER_ROLE, role.name())
			.signWith(secretKey)
			.compact();
	}

	public String generateRefreshToken(Long id, String email) {
		return Jwts.builder()
			.issuer(tokenConfig.getIssuer())
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + tokenConfig.getRefreshTokenExpiration()))
			.claim(CLAIM_MEMBER_ID, id)
			.claim(CLAIM_MEMBER_EMAIL, email)
			.signWith(secretKey)
			.compact();
	}

	public String extractToken(String authorizationHeader) {
		if (!StringUtils.hasText(authorizationHeader)) {
			throw new UnauthorizedException(
				ErrorCode.TOKEN_INVALID,
				ErrorMessage.TOKEN_INVALID
			);
		}

		if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
			throw new UnauthorizedException(
				ErrorCode.TOKEN_INVALID,
				ErrorMessage.TOKEN_INVALID
			);
		}

		return authorizationHeader.substring(7);
	}

	public Claims parseClaims(String token) {
		try {
			return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();

		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
}
