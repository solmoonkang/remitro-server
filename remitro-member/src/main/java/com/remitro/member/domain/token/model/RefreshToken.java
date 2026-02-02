package com.remitro.member.domain.token.model;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

	private Long memberId;

	private String token;

	private LocalDateTime expiresAt;

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
