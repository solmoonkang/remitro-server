package com.remitro.account.infrastructure.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ValueRedisRepository {

	public static final String IDEMPOTENCY_PREFIX = "IDEMPOTENCY_PREFIX:";

	private final RedisTemplate<String, String> stringRedisTemplate;

	public boolean setIfAbsent(String idempotencyKey, String value, long expirationTime) {
		Boolean isNewKey = stringRedisTemplate.opsForValue()
			.setIfAbsent(generateIdempotencyKey(idempotencyKey), value, expirationTime, TimeUnit.SECONDS);

		return Boolean.TRUE.equals(isNewKey);
	}

	private String generateIdempotencyKey(String idempotencyKey) {
		return IDEMPOTENCY_PREFIX + idempotencyKey;
	}
}
