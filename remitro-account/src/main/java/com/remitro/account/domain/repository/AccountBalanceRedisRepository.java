package com.remitro.account.domain.repository;

import static com.remitro.common.util.constant.RedisConstant.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.remitro.account.infrastructure.redis.ValueRedisRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountBalanceRedisRepository {

	private final ValueRedisRepository valueRedisRepository;

	public void saveCachedBalance(Long accountId, Long balance) {
		valueRedisRepository.setValue(generateBalanceCacheKey(accountId), String.valueOf(balance), BALANCE_CACHE_TTL);
	}

	public Optional<Long> findCachedBalance(Long accountId) {
		final String balance = valueRedisRepository.getValue(generateBalanceCacheKey(accountId));

		if (balance == null) {
			return Optional.empty();
		}

		return Optional.of(Long.parseLong(balance));
	}

	public void evictCachedBalance(Long accountId) {
		valueRedisRepository.deleteValue(generateBalanceCacheKey(accountId));
	}

	private String generateBalanceCacheKey(Long accountId) {
		return BALANCE_CACHE_PREFIX + accountId;
	}
}
