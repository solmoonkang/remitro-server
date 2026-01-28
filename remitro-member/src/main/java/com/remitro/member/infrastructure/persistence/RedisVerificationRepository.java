package com.remitro.member.infrastructure.persistence;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remitro.member.domain.verification.model.Verification;
import com.remitro.member.domain.verification.repository.VerificationRepository;
import com.remitro.support.util.JsonSupport;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisVerificationRepository implements VerificationRepository {

	private static final String VERIFICATION_PREFIX = "VERIFICATION:";
	private static final Duration VERIFICATION_EXPIRATION_TIME = Duration.ofMinutes(5);

	private final StringRedisTemplate stringRedisTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public Optional<Verification> findByEmail(String email) {
		return Optional.ofNullable(stringRedisTemplate.opsForValue().get(
				generateKey(email))
			)
			.map(jsonData -> JsonSupport.fromJSON(objectMapper, jsonData, Verification.class));
	}

	@Override
	public void save(Verification verification) {
		stringRedisTemplate.opsForValue().set(
			generateKey(verification.getEmail()),
			JsonSupport.toJSON(objectMapper, verification),
			VERIFICATION_EXPIRATION_TIME
		);
	}

	@Override
	public void deleteByEmail(String email) {
		stringRedisTemplate.delete(
			generateKey(email)
		);
	}

	private String generateKey(String email) {
		return VERIFICATION_PREFIX + email;
	}
}
