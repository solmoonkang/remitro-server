package com.remitro.account.infrastructure.persistence;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remitro.account.domain.idempotency.model.Idempotency;
import com.remitro.account.domain.idempotency.repository.IdempotencyRepository;
import com.remitro.support.util.JsonSupport;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisIdempotencyRepository implements IdempotencyRepository {

	private static final String IDEMPOTENCY_PREFIX = "IDEMPOTENCY:";
	private static final Duration IDEMPOTENCY_EXPIRATION_TIME = Duration.ofHours(24);

	private final StringRedisTemplate stringRedisTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public boolean saveIfAbsent(Idempotency idempotency) {
		return Boolean.TRUE.equals(
			stringRedisTemplate.opsForValue().setIfAbsent(
				generateKey(idempotency.getRequestId()),
				JsonSupport.toJSON(objectMapper, idempotency),
				IDEMPOTENCY_EXPIRATION_TIME
			)
		);
	}

	@Override
	public void deleteByRequestId(String requestId) {
		stringRedisTemplate.delete(
			generateKey(requestId)
		);
	}

	private String generateKey(String requestId) {
		return IDEMPOTENCY_PREFIX + requestId;
	}
}
