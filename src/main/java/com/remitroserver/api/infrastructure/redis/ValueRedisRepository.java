package com.remitroserver.api.infrastructure.redis;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ValueRedisRepository {

	private final StringRedisTemplate stringRedisTemplate;

	public void save(String key, String value, Duration expiration) {
		stringRedisTemplate.opsForValue().set(key, value, expiration);
	}

	public String find(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	public void delete(String key) {
		stringRedisTemplate.delete(key);
	}

	public Long increment(String key) {
		return stringRedisTemplate.opsForValue().increment(key, 1);
	}

	public void expire(String key, Duration expiration) {
		stringRedisTemplate.expire(key, expiration);
	}

	public Long getExpire(String key, TimeUnit timeUnit) {
		return stringRedisTemplate.getExpire(key, timeUnit);
	}
}
