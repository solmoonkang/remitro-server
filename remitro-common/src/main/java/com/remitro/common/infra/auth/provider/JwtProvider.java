package com.remitro.common.infra.auth.provider;

import static com.remitro.common.infra.util.JwtClaimsConstant.*;
import static com.remitro.common.infra.util.TokenConstant.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Consumer;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.remitro.common.infra.auth.model.AuthMember;
import com.remitro.common.infra.config.TokenConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
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

	public String generateAccessToken(Long id, String email, String nickname) {
		return buildJwt(tokenConfig.getAccessTokenExpiration(), claims -> claims
			.claim(MEMBER_ID, id)
			.claim(MEMBER_EMAIL, email)
			.claim(MEMBER_NICKNAME, nickname)
		);
	}

	public String generateRefreshToken(String email) {
		return buildJwt(tokenConfig.getRefreshTokenExpiration(), claims -> claims
			.claim(MEMBER_EMAIL, email)
		);
	}

	public AuthMember extractAuthMemberFromToken(String token) {
		Claims claims = parseClaims(token);

		Long id = claims.get(MEMBER_ID, Long.class);
		String email = claims.get(MEMBER_EMAIL, String.class);
		String nickname = claims.get(MEMBER_NICKNAME, String.class);

		return new AuthMember(id, email, nickname);
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token);

			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("[✅ LOGGER] 잘못된 서명 또는 구조입니다.", e);
		} catch (ExpiredJwtException e) {
			log.info("[✅ LOGGER] 만료된 토큰입니다.", e);
		} catch (UnsupportedJwtException e) {
			log.info("[✅ LOGGER] 지원하지 않는 토큰입니다.", e);
		} catch (IllegalArgumentException e) {
			log.info("[✅ LOGGER] 잘못된 JWT 토큰입니다.", e);
		}

		return false;
	}

	public String extractToken(HttpServletRequest httpServletRequest) {
		String bearer = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

		if (StringUtils.hasText(bearer) && bearer.startsWith(BEARER_PREFIX)) {
			return bearer.substring(7);
		}

		return null;
	}

	private String buildJwt(long expirationTime, Consumer<JwtBuilder> claimsConsumer) {
		JwtBuilder jwtBuilder = createBaseBuilder(expirationTime);
		claimsConsumer.accept(jwtBuilder);
		return jwtBuilder.compact();
	}

	private JwtBuilder createBaseBuilder(long expirationTime) {
		Date issuedDate = new Date();
		Date expiredDate = new Date(issuedDate.getTime() + expirationTime);

		return Jwts.builder()
			.issuer(tokenConfig.getIssuer())
			.issuedAt(issuedDate)
			.expiration(expiredDate)
			.signWith(secretKey);
	}

	private Claims parseClaims(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
}
