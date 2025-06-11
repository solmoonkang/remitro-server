package com.remitroserver.api.domain.transaction.repository;

import static com.remitroserver.global.common.util.RedisConstant.*;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.remitroserver.api.domain.transaction.model.TransactionStatus;
import com.remitroserver.api.infrastructure.redis.ValueRedisRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class IdempotencyKeyRedisRepository {

	private final ValueRedisRepository valueRedisRepository;

	public void saveIdempotencyKey(String idempotencyKey) {
		valueRedisRepository.save(
			generateIdempotencyKey(idempotencyKey),
			TransactionStatus.COMPLETED.name(),
			IDEMPOTENCY_EXPIRATION);
	}

	public void saveTransactionTokenMapping(UUID transactionToken, String idempotencyKey) {
		valueRedisRepository.save(
			generateTransactionTokenKey(transactionToken),
			idempotencyKey,
			IDEMPOTENCY_EXPIRATION);
	}

	public String findIdempotencyKeyByTransactionToken(UUID transactionToken) {
		return valueRedisRepository.find(generateTransactionTokenKey(transactionToken));
	}

	public boolean isIdempotencyKeyExists(String idempotencyKey) {
		return valueRedisRepository.find(generateIdempotencyKey(idempotencyKey)) != null;
	}

	private String generateIdempotencyKey(String idempotencyKey) {
		return IDEMPOTENCY_KEY_PREFIX + idempotencyKey;
	}

	private String generateTransactionTokenKey(UUID transactionToken) {
		return IDEMPOTENCY_TRANSACTION_TOKEN_KEY_PREFIX + transactionToken;
	}
}
