package com.remitro.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;

@Getter
@ConfigurationProperties(prefix = "jwt")
public class TokenConfig {

	private final String secret;

	private final long accessTokenExpiration;

	private final long refreshTokenExpiration;

	private final String issuer;

	public TokenConfig(String secret, long accessTokenExpiration, long refreshTokenExpiration, String issuer) {
		this.secret = secret;
		this.accessTokenExpiration = accessTokenExpiration;
		this.refreshTokenExpiration = refreshTokenExpiration;
		this.issuer = issuer;
	}
}

