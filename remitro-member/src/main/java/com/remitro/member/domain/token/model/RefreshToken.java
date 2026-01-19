package com.remitro.member.domain.token.model;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class RefreshToken {

	private final Long memberId;

	private final String token;

	private final LocalDateTime expiresAt;

	private RefreshToken(Long memberId, String token, LocalDateTime expiresAt) {
		this.memberId = memberId;
		this.token = token;
		this.expiresAt = expiresAt;
	}

	public static RefreshToken issue(Long memberId, String token, long expiresInMillis, LocalDateTime issuedAt) {
		return new RefreshToken(memberId, token, issuedAt.plus(Duration.ofMillis(expiresInMillis)));
	}

	public static RefreshToken reconstruct(Long memberId, String token) {
		return new RefreshToken(memberId, token, null);
	}

	public long secondsUntilExpiration(LocalDateTime now) {
		long seconds = Duration.between(now, expiresAt).toSeconds();
		return Math.max(seconds, 0);
	}
}
