package com.remitroserver.api.infrastructure.redis;

import java.time.Duration;
import java.util.List;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ListRedisRepository {

	private final StringRedisTemplate stringRedisTemplate;

	public void leftPush(String key, String value) {
		stringRedisTemplate.opsForList().leftPush(key, value);
	}

	public void leftPushWithExpire(String key, String value, Duration expiration) {
		stringRedisTemplate.opsForList().leftPush(key, value);
		stringRedisTemplate.expire(key, expiration);
	}

	public String rightPop(String key) {
		return stringRedisTemplate.opsForList().rightPop(key);
	}

	public List<String> range(String key, long start, long end) {
		return stringRedisTemplate.opsForList().range(key, start, end);
	}

	public void trim(String key, long start, long end) {
		stringRedisTemplate.opsForList().trim(key, start, end);
	}

	public Long size(String key) {
		return stringRedisTemplate.opsForList().size(key);
	}
}
