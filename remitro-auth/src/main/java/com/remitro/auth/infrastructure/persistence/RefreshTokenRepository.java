package com.remitro.auth.infrastructure.persistence;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remitro.auth.domain.model.RefreshToken;
import com.remitro.common.util.JsonMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

	private static final String PREFIX = "REFRESH:TOKEN:";

	private final StringRedisTemplate stringRedisTemplate;
	private final ObjectMapper objectMapper;

	public void save(RefreshToken refreshToken) {
		stringRedisTemplate.opsForValue().set(
			generateKey(refreshToken.token()),
			JsonMapper.toJSON(objectMapper, refreshToken),
			Duration.ofMillis(refreshToken.expirationTime())
		);
	}

	public Optional<RefreshToken> findByToken(String token) {
		return Optional.of(stringRedisTemplate.opsForValue().get(generateKey(token)))
			.map(value -> JsonMapper.fromJSON(objectMapper, value, RefreshToken.class));
	}

	public void revoke(String token) {
		stringRedisTemplate.delete(generateKey(token));
	}

	public void revokeByMemberAndDevice(Long memberId, String deviceId) {
		stringRedisTemplate.keys(PREFIX + "*").forEach(key -> {
			RefreshToken refreshToken = JsonMapper.fromJSON(
				objectMapper,
				stringRedisTemplate.opsForValue().get(key),
				RefreshToken.class
			);

			if (refreshToken.memberId().equals(memberId) && refreshToken.deviceId().equals(deviceId)) {
				stringRedisTemplate.delete(key);
			}
		});
	}

	public void revokeAllByMember(Long memberId) {
		stringRedisTemplate.keys(PREFIX + "*").forEach(key -> {
			RefreshToken refreshToken = JsonMapper.fromJSON(
				objectMapper,
				stringRedisTemplate.opsForValue().get(key),
				RefreshToken.class
			);

			if (refreshToken.memberId().equals(memberId)) {
				stringRedisTemplate.delete(key);
			}
		});
	}

	private String generateKey(String token) {
		return PREFIX + token;
	}
}
