package com.remitro.auth.infrastructure.redis;

import java.time.Duration;
import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ValueRedisRepository {

	private final StringRedisTemplate stringRedisTemplate;

	public void set(String key, String value, Duration expirationTime) {
		stringRedisTemplate.opsForValue().set(key, value, expirationTime);
	}

	public String get(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	public void delete(String key) {
		stringRedisTemplate.delete(key);
	}

	public Set<String> scanKeys(String pattern) {
		return stringRedisTemplate.keys(pattern);
	}
}
