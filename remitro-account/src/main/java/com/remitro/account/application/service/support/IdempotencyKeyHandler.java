package com.remitro.account.application.service.support;

import org.springframework.stereotype.Service;

import com.remitro.account.infrastructure.redis.ValueRedisRepository;
import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdempotencyKeyHandler {

	public static final long IDEMPOTENCY_EXPIRATION_TIME = 3600;

	private final ValueRedisRepository valueRedisRepository;

	public void preventDuplicateRequestAndRecordKey(String idempotencyKey) {
		validateIdempotencyKeyIsPresent(idempotencyKey);
		boolean isNewKey = valueRedisRepository.setIfAbsent(idempotencyKey, "processed", IDEMPOTENCY_EXPIRATION_TIME);
		validateKeyIsNotDuplicate(isNewKey);
	}

	private void validateIdempotencyKeyIsPresent(String idempotencyKey) {
		if (idempotencyKey == null || idempotencyKey.isEmpty()) {
			throw new ConflictException(ErrorMessage.IDEMPOTENCY_KEY_MISSING);
		}
	}

	private void validateKeyIsNotDuplicate(boolean isNewKey) {
		if (!isNewKey) {
			throw new ConflictException(ErrorMessage.DUPLICATE_IDEMPOTENCY_REQUEST);
		}
	}
}
