package com.remitro.account.application.service;

import static com.remitro.common.infrastructure.util.constant.GlobalConstant.*;
import static com.remitro.common.infrastructure.util.constant.RedisConstant.*;

import org.springframework.stereotype.Service;

import com.remitro.account.infrastructure.redis.ValueRedisRepository;
import com.remitro.common.infrastructure.error.exception.ConflictException;
import com.remitro.common.infrastructure.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

	private final ValueRedisRepository valueRedisRepository;

	public void validateOpenAccountIdempotency(Long memberId, String idempotencyKey, String prefix) {
		if (idempotencyKey == null || idempotencyKey.isBlank()) {
			return;
		}

		final String openAccountIdempotencyKey = generateOpenAccountKey(memberId, idempotencyKey, prefix);
		validateFirstIdempotentRequest(openAccountIdempotencyKey, OPEN_ACCOUNT_IDEMPOTENCY_TTL);
	}

	public void validateBalanceChangeIdempotency(Long memberId, Long accountId, String idempotencyKey, String prefix) {
		if (idempotencyKey == null || idempotencyKey.isBlank()) {
			return;
		}

		final String balanceIdempotencyKey = generateBalanceChangeKey(memberId, accountId, idempotencyKey, prefix);
		validateFirstIdempotentRequest(balanceIdempotencyKey, BALANCE_CHANGE_IDEMPOTENCY_TTL);
	}

	public void validateTransferIdempotency(Long memberId, Long accountId, String idempotencyKey, String prefix) {
		if (idempotencyKey == null || idempotencyKey.isBlank()) {
			return;
		}

		final String transferIdempotencyKey = generateTransferKey(memberId, accountId, idempotencyKey, prefix);
		validateFirstIdempotentRequest(transferIdempotencyKey, TRANSFER_IDEMPOTENCY_TTL);
	}

	private void validateFirstIdempotentRequest(String redisKey, long expirationSeconds) {
		boolean isFirstRequest = valueRedisRepository.setIfAbsent(
			redisKey,
			IDEMPOTENCY_MARKER_VALUE,
			expirationSeconds
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

	private String generateTransferKey(Long memberId, Long accountId, String idempotencyKey, String prefix) {
		return prefix
			+ memberId + SEPARATOR
			+ accountId + SEPARATOR
			+ idempotencyKey;
	}
}
