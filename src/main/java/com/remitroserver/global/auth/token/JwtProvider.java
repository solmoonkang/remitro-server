package com.remitroserver.global.auth.token;

import static com.remitroserver.global.common.util.JwtConstant.*;
import static com.remitroserver.global.error.model.ErrorMessage.*;
import static java.nio.charset.StandardCharsets.*;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.remitroserver.api.domain.auth.model.AuthMember;
import com.remitroserver.global.error.exception.UnauthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
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

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.issuer}")
	private String issuer;

	@Value("${jwt.access-token-expiration}")
	private Duration accessTokenExpiration;

	@Value("${jwt.refresh-token-expiration}")
	private Duration refreshTokenExpiration;

	private Key signingKey;

	@PostConstruct
	public void init() {
		byte[] keyBytes = secretKey.getBytes(UTF_8);
		this.signingKey = Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateAccessToken(String email, String nickname) {
		return buildJwt(accessTokenExpiration, claims -> claims
			.claim(CLAIM_EMAIL, email)
			.claim(CLAIM_NICKNAME, nickname));
	}

	public String generateRefreshToken(String email) {
		return buildJwt(refreshTokenExpiration, claims -> claims
			.claim(CLAIM_EMAIL, email));
	}

	public AuthMember extractAuthMemberFromToken(String token) {
		Claims cLaims = parseClaims(token);

		String email = cLaims.get(CLAIM_EMAIL, String.class);
		String nickname = cLaims.get(CLAIM_NICKNAME, String.class);

		return new AuthMember(email, nickname);
	}

	public String extractEmailFromToken(String token) {
		Claims claims = parseClaims(token);
		return claims.get(CLAIM_EMAIL, String.class);
	}

	public String extractToken(HttpServletRequest httpServletRequest, String headerName) {
		String token = httpServletRequest.getHeader(headerName);

		if (StringUtils.hasText(token) && token.startsWith(BEARER_TYPE)) {
			return token.substring(BEARER_TYPE.length()).trim();
		}

		throw new UnauthorizedException(INVALID_AUTHORIZATION_HEADER_ERROR);
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.build()
				.parseClaimsJws(token);

			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.warn("[✅ LOGGER: JWT PROVIDER] 잘못된 JWT 서명입니다.", e);
		} catch (ExpiredJwtException e) {
			log.warn("[✅ LOGGER: JWT PROVIDER] 만료된 JWT 토큰입니다.", e);
		} catch (UnsupportedJwtException e) {
			log.warn("[✅ LOGGER: JWT PROVIDER] 지원하지 않는 JWT 토큰입니다.", e);
		} catch (IllegalArgumentException e) {
			log.warn("[✅ LOGGER: JWT PROVIDER] 잘못된 JWT 토큰입니다.", e);
		} catch (Exception e) {
			log.warn("[✅ LOGGER: JWT PROVIDER] 유효하지 않은 JWT 토큰입니다.", e);
			throw new UnauthorizedException(INVALID_AUTHORIZATION_HEADER_ERROR);
		}

		return false;
	}

	private String buildJwt(Duration expirationTime, Consumer<JwtBuilder> claimsConsumer) {
		Date issuedDate = new Date();
		Date expiryDate = new Date(issuedDate.getTime() + expirationTime.toMillis());

		JwtBuilder jwtBuilder = Jwts.builder()
			.setIssuer(issuer)
			.setIssuedAt(issuedDate)
			.setExpiration(expiryDate)
			.signWith(signingKey, SignatureAlgorithm.HS256);

		claimsConsumer.accept(jwtBuilder);
		return jwtBuilder.compact();
	}

	private Claims parseClaims(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
}
