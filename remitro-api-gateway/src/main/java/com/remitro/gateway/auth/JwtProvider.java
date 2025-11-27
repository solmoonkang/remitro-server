package com.remitro.gateway.auth;

import static com.remitro.gateway.constant.AuthorizationConstant.*;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.remitro.gateway.config.TokenConfig;

import io.jsonwebtoken.Claims;
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

	public boolean isValidToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token);

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public String extractToken(ServerHttpRequest serverHttpRequest) {
		String bearer = serverHttpRequest.getHeaders().getFirst(AUTHORIZATION_HEADER);

		if (StringUtils.hasText(bearer) && bearer.startsWith(BEARER_PREFIX)) {
			return bearer.substring(7);
		}

		return null;
	}

	public Claims extractClaims(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
}
