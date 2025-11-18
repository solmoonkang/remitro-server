package com.remitro.account.application.service;

import org.springframework.stereotype.Service;

import com.remitro.account.infrastructure.redis.ValueRedisRepository;
import com.remitro.common.infra.error.exception.ConflictException;
import com.remitro.common.infra.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

	public static final String IDEMPOTENCY_MARKER_VALUE = "LOCK";
	public static final long OPEN_ACCOUNT_IDEMPOTENCY_EXPIRATION_SECONDS = 60L * 5L;
	public static final String SEPARATOR = ":";

	private final ValueRedisRepository valueRedisRepository;

	public void validateIdempotencyFirstRequest(Long memberId, String idempotencyKey, String prefix) {
		if (idempotencyKey == null || idempotencyKey.isBlank()) {
			return;
		}

		boolean isFirstRequest = valueRedisRepository.setIfAbsent(
			generateIdempotencyKey(memberId, idempotencyKey, prefix),
			IDEMPOTENCY_MARKER_VALUE,
			OPEN_ACCOUNT_IDEMPOTENCY_EXPIRATION_SECONDS
		);
		if (!isFirstRequest) {
			throw new ConflictException(ErrorMessage.DUPLICATE_IDEMPOTENCY_REQUEST);
		}
	}

	private String generateIdempotencyKey(Long memberId, String idempotencyKey, String prefix) {
		return prefix + memberId + SEPARATOR + idempotencyKey;
	}
}
