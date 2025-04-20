package com.remitroserver.api.infrastructure.redis;

import java.time.Duration;

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
}
