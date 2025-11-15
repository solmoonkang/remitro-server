package com.remitro.account.application.service;

import org.springframework.stereotype.Service;

import com.remitro.account.infrastructure.redis.ValueRedisRepository;
import com.remitro.common.infra.error.exception.ConflictException;
import com.remitro.common.infra.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OpenAccountIdempotencyService {

	public static final long OPEN_ACCOUNT_IDEMPOTENCY_EXPIRATION_SECONDS = 60L * 5L;
	public static final String OPEN_ACCOUNT_PREFIX = "OPEN_ACCOUNT:";

	private final ValueRedisRepository valueRedisRepository;

	public void validateOpenAccountIdempotency(Long memberId, String idempotencyKey) {
		if (idempotencyKey == null || idempotencyKey.isBlank()) {
			return;
		}

		final String openAccountKey = generateOpenAccountKey(memberId, idempotencyKey);
		final boolean isFirstRequest = valueRedisRepository.setIfAbsent(
			openAccountKey,
			"LOCK",
			OPEN_ACCOUNT_IDEMPOTENCY_EXPIRATION_SECONDS
		);

		if (!isFirstRequest) {
			throw new ConflictException(ErrorMessage.DUPLICATE_IDEMPOTENCY_REQUEST);
		}
	}

	private String generateOpenAccountKey(Long memberId, String idempotencyKey) {
		return OPEN_ACCOUNT_PREFIX + memberId + ":" + idempotencyKey;
	}
}
