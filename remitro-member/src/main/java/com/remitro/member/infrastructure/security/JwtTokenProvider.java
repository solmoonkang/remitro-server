package com.remitro.member.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.InternalServerException;
import com.remitro.common.exception.UnauthorizedException;
import com.remitro.common.security.AuthenticatedUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	private static final String CLAIM_TOKEN_TYPE = "type";
	private static final String TOKEN_TYPE_ACCESS = "ACCESS";
	private static final String TOKEN_TYPE_REFRESH = "REFRESH";

	private final JwtProperties jwtProperties;
	private final SecretKey secretKey;

	public JwtTokenProvider(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
		this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
	}

	public String issueAccessToken(Long memberId) {
		return issueToken(memberId, TOKEN_TYPE_ACCESS, jwtProperties.accessTokenExpirationTime());
	}

	public String issueRefreshToken(Long memberId) {
		return issueToken(memberId, TOKEN_TYPE_REFRESH, jwtProperties.refreshTokenExpirationTime());
	}

	private String issueToken(Long memberId, String type, long expirationTime) {
		return Jwts.builder()
			.subject(String.valueOf(memberId))
			.issuer(jwtProperties.issuer())
			.claim(CLAIM_TOKEN_TYPE, type)
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + expirationTime))
			.signWith(secretKey)
			.compact();
	}

	public AuthenticatedUser authenticate(String accessToken) {
		final Claims claims = getVerifiedClaims(accessToken, TOKEN_TYPE_ACCESS);
		return AuthenticatedUser.of(Long.parseLong(claims.getSubject()));
	}

	public Long extractMemberId(String refreshToken) {
		final Claims claims = getVerifiedClaims(refreshToken, TOKEN_TYPE_REFRESH);
		return Long.parseLong(claims.getSubject());
	}

	private Claims getVerifiedClaims(String token, String expectedTokenType) {
		try {
			Claims claims = Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();

			String actualTokenType = claims.get(CLAIM_TOKEN_TYPE, String.class);

			if (!expectedTokenType.equals(actualTokenType)) {
				throw new UnauthorizedException(ErrorCode.INVALID_TOKEN, "타입");
			}

			return claims;

		} catch (ExpiredJwtException e) {
			log.info("[✅ LOGGER] JWT 토큰이 만료되었습니다. ID = {}", e.getClaims().getSubject());
			throw new UnauthorizedException(ErrorCode.EXPIRED_TOKEN);
		} catch (JwtException e) {
			log.warn("[✅ LOGGER] 유효하지 않은 JWT 토큰으로 접근을 시도했습니다: {}", e.getMessage());
			throw new UnauthorizedException(ErrorCode.INVALID_TOKEN, "보안 구성");
		} catch (Exception e) {
			log.error("[✅ LOGGER] JWT 시스템 오류가 발생했습니다: {}", e.getMessage());
			throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	public long getRefreshTokenExpirationTime() {
		return jwtProperties.refreshTokenExpirationTime();
	}
}
