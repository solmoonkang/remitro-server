package com.remitro.member.infrastructure.persistence;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.remitro.member.domain.token.model.RefreshToken;
import com.remitro.member.domain.token.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisRefreshTokenRepository implements RefreshTokenRepository {

	private static final String REFRESH_TOKEN_PREFIX = "REFRESH_TOKEN:";

	private final StringRedisTemplate stringRedisTemplate;

	@Override
	public void save(RefreshToken refreshToken) {
		long secondsUntilExpiration = refreshToken.secondsUntilExpiration(LocalDateTime.now());

		stringRedisTemplate.opsForValue().set(
			generateKey(refreshToken.getMemberId()),
			refreshToken.getToken(),
			Duration.ofSeconds(secondsUntilExpiration)
		);
	}

	@Override
	public void deleteByMemberId(Long memberId) {
		stringRedisTemplate.delete(generateKey(memberId));
	}

	private String generateKey(Long memberId) {
		return REFRESH_TOKEN_PREFIX + memberId;
	}
}
