package com.remitroserver.api.domain.account.repository;

import static com.remitroserver.global.common.util.RedisConstant.*;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Repository;

import com.remitroserver.api.infrastructure.redis.ValueRedisRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountRedisRepository {

	private final ValueRedisRepository valueRedisRepository;

	public long incrementAccountFailedAttempts(Long accountId) {
		Long failedCount = valueRedisRepository.increment(generateAccountFailKey(accountId));
		valueRedisRepository.expire(generateAccountFailKey(accountId), ACCOUNT_LOCK_EXPIRATION);

		if (failedCount == null) {
			return 0;
		}

		return failedCount;
	}

	public void resetFailedAttempts(Long accountId) {
		valueRedisRepository.delete(generateAccountFailKey(accountId));
	}

	public void lockAccount(Long accountId) {
		valueRedisRepository.save(generateAccountLockKey(accountId), "LOCKED", ACCOUNT_LOCK_EXPIRATION);
	}

	public boolean isAccountLocked(Long accountId) {
		return valueRedisRepository.find(generateAccountLockKey(accountId)) != null;
	}

	public long getLockRemainingMinutes(Long accountId) {
		Long seconds = valueRedisRepository.getExpire(generateAccountLockKey(accountId), TimeUnit.SECONDS);

		if (seconds == null || seconds <= 0) {
			return 0;
		}

		return (seconds + SECONDS_PER_MINUTE - 1) / SECONDS_PER_MINUTE;
	}

	private String generateAccountFailKey(Long accountId) {
		return ACCOUNT_FAIL_KEY_PREFIX + accountId;
	}

	private String generateAccountLockKey(Long accountId) {
		return ACCOUNT_LOCK_KEY_PREFIX + accountId;
	}
}
