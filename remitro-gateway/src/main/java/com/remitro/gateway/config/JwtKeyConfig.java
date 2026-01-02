package com.remitro.gateway.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtKeyConfig {

	private final JwtConfig jwtConfig;

	@Bean
	public SecretKey jwtSecretKey() {
		return Keys.hmacShaKeyFor(
			jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8)
		);
	}
}
