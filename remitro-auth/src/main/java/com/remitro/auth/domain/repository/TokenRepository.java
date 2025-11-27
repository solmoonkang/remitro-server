package com.remitro.auth.domain.repository;

import java.time.Duration;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.remitro.auth.domain.model.RefreshToken;
import com.remitro.auth.infrastructure.redis.ValueRedisRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TokenRepository {

	public static final String REFRESH_TOKEN_PREFIX = "REFRESH:TOKEN:";

	private final ValueRedisRepository valueRedisRepository;

	public void saveToken(RefreshToken refreshToken) {
		valueRedisRepository.set(
			generateRefreshTokenKey(refreshToken.memberId()),
			refreshToken.token(),
			Duration.ofMillis(refreshToken.expirationTime())
		);
	}

	public Optional<RefreshToken> findTokenByMemberId(Long memberId) {
		String token = valueRedisRepository.get(generateRefreshTokenKey(memberId));

		if (token == null) {
			return Optional.empty();
		}

		return Optional.of(new RefreshToken(memberId, token, 0L));
	}

	public void deleteToken(Long memberId) {
		valueRedisRepository.delete(generateRefreshTokenKey(memberId));
	}

	private String generateRefreshTokenKey(Long memberId) {
		return REFRESH_TOKEN_PREFIX + memberId;
	}
}
