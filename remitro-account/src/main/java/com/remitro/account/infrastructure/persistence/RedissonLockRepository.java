package com.remitro.account.infrastructure.persistence;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import com.remitro.account.domain.account.repository.TransactionLockRepository;
import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.InternalServerException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedissonLockRepository implements TransactionLockRepository {

	private static final String TRANSACTION_LOCK_PREFIX = "TRANSACTION:LOCK:";

	private final RedissonClient redissonClient;

	@Override
	public void acquireLock(Long accountId, long waitTime, long leaseTime) {
		final RLock lock = redissonClient.getLock(generateLockKey(accountId));

		try {
			final boolean isAcquired = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);

			if (!isAcquired) {
				throw new InternalServerException(ErrorCode.LOCK_ACQUISITION_FAILED);
			}

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void releaseLock(Long accountId) {
		final RLock lock = redissonClient.getLock(generateLockKey(accountId));

		if (lock.isLocked() && lock.isHeldByCurrentThread()) {
			lock.unlock();
		}
	}

	private String generateLockKey(Long accountId) {
		return TRANSACTION_LOCK_PREFIX + accountId;
	}
}
