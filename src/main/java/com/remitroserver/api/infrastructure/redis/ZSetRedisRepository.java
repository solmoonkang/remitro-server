package com.remitroserver.api.infrastructure.redis;

import java.time.Duration;
import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ZSetRedisRepository {

	private final StringRedisTemplate stringRedisTemplate;

	public void add(String key, String value, double score) {
		stringRedisTemplate.opsForZSet().add(key, value, score);
	}

	public void addWithExpire(String key, String value, double score, Duration expiration) {
		stringRedisTemplate.opsForZSet().add(key, value, score);
		stringRedisTemplate.expire(key, expiration);
	}

	public Set<String> rangeByScore(String key, double min, double max) {
		return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
	}

	public void remove(String key, String value) {
		stringRedisTemplate.opsForZSet().remove(key, value);
	}

	public Long count(String key, double min, double max) {
		return stringRedisTemplate.opsForZSet().count(key, min, max);
	}
}
