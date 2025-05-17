package com.remitroserver.api.domain.transaction.repository;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.remitroserver.api.dto.transaction.request.TransferRequest;
import com.remitroserver.api.infrastructure.redis.ZSetRedisRepository;
import com.remitroserver.global.common.util.JsonConverter;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TransactionRedisRepository {

	public static final String TRANSACTION_KEY_PREFIX = "TRANSACTION_PENDING:";
	public static final Duration TRANSACTION_EXPIRATION = Duration.ofMinutes(10);

	private final ZSetRedisRepository zSetRedisRepository;
	private final JsonConverter jsonConverter;

	public void saveTransaction(UUID transactionToken, TransferRequest transferRequest) {
		final String serializationJsonMessage = jsonConverter.toJson(transferRequest);
		zSetRedisRepository.addWithExpire(
			generateTransactionKey(transactionToken), serializationJsonMessage, 0, TRANSACTION_EXPIRATION);
	}

	public Set<String> getTransactionInRange(UUID transactionToken, long start, long end) {
		return zSetRedisRepository.rangeByScore(generateTransactionKey(transactionToken), start, end);
	}

	public void deleteTransaction(UUID transactionToken, String transactionJson) {
		zSetRedisRepository.remove(generateTransactionKey(transactionToken), transactionJson);
	}

	private String generateTransactionKey(UUID transactionToken) {
		return TRANSACTION_KEY_PREFIX + transactionToken;
	}
}
