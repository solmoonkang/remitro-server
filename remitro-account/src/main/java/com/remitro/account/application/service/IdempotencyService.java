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
	public static final long DEFAULT_IDEMPOTENCY_EXPIRATION_SECONDS = 60L * 5L;
	public static final String SEPARATOR = ":";

	private final ValueRedisRepository valueRedisRepository;

	public void validateOpenAccountIdempotency(Long memberId, String idempotencyKey, String prefix) {
		if (idempotencyKey == null || idempotencyKey.isBlank()) {
			return;
		}

		final String openAccountIdempotencyKey = generateOpenAccountKey(memberId, idempotencyKey, prefix);
		validateFirstIdempotentRequest(openAccountIdempotencyKey);
	}

	public void validateBalanceChangeIdempotency(Long memberId, Long accountId, String idempotencyKey, String prefix) {
		if (idempotencyKey == null || idempotencyKey.isBlank()) {
			return;
		}

		final String balanceIdempotencyKey = generateBalanceChangeKey(memberId, accountId, idempotencyKey, prefix);
		validateFirstIdempotentRequest(balanceIdempotencyKey);
	}

	private void validateFirstIdempotentRequest(String redisKey) {
		boolean isFirstRequest = valueRedisRepository.setIfAbsent(
			redisKey,
			IDEMPOTENCY_MARKER_VALUE,
			DEFAULT_IDEMPOTENCY_EXPIRATION_SECONDS
		);

		if (!isFirstRequest) {
			throw new ConflictException(ErrorMessage.DUPLICATE_IDEMPOTENCY_REQUEST);
		}
	}

	private String generateOpenAccountKey(Long memberId, String idempotencyKey, String prefix) {
		return prefix
			+ memberId + SEPARATOR
			+ idempotencyKey;
	}

	private String generateBalanceChangeKey(Long memberId, Long accountId, String idempotencyKey, String prefix) {
		return prefix
			+ memberId + SEPARATOR
			+ accountId + SEPARATOR
			+ idempotencyKey;
	}
}
