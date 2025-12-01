package com.remitro.account.infrastructure.redis;

import static com.remitro.common.util.constant.RedisConstant.*;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ValueRedisRepository {

	private final RedisTemplate<String, String> stringRedisTemplate;

	public void setValue(String key, String value, long expirationTime) {
		stringRedisTemplate.opsForValue()
			.set(key, value, expirationTime, TimeUnit.SECONDS);
	}

	public String getValue(String key) {
		return stringRedisTemplate.opsForValue()
			.get(key);
	}

	public void deleteValue(String key) {
		stringRedisTemplate.delete(key);
	}

	public boolean setIfAbsent(String openAccountKey, String value, long expirationTime) {
		Boolean isNewKey = stringRedisTemplate.opsForValue()
			.setIfAbsent(generateIdempotencyKey(openAccountKey), value, expirationTime, TimeUnit.SECONDS);

		return Boolean.TRUE.equals(isNewKey);
	}

	private String generateIdempotencyKey(String openAccountKey) {
		return IDEMPOTENCY_PREFIX + openAccountKey;
	}
}
