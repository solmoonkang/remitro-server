package com.remitro.auth.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.remitro.auth.infrastructure.config.JwtConfig;
import com.remitro.common.auth.MemberAuthInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenService {

	private final JwtConfig jwtConfig;
	private SecretKey secretKey;

	@PostConstruct
	void init() {
		this.secretKey = Keys.hmacShaKeyFor(
			jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8)
		);
	}

	public String issueAccessToken(MemberAuthInfo info) {
		return Jwts.builder()
			.issuer(jwtConfig.getIssuer())
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + jwtConfig.getAccessTokenExpiration()))
			.claim("memberId", info.memberId())
			.claim("email", info.email())
			.claim("nickname", info.nickname())
			.claim("role", info.role().name())
			.signWith(secretKey)
			.compact();
	}

	public String issueRefreshToken(MemberAuthInfo info) {
		return Jwts.builder()
			.issuer(jwtConfig.getIssuer())
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiration()))
			.claim("memberId", info.memberId())
			.claim("email", info.email())
			.signWith(secretKey)
			.compact();
	}

	public Claims parseClaims(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
}
