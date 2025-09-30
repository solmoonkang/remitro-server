package com.remitro.account.infrastructure.redis;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.springframework.stereotype.Component;

import com.remitro.account.domain.repository.DistributedLockRepository;
import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DistributedLockManager {

	public static final long MAX_LOCK_WAIT_TIME = 2000L;
	public static final long MAX_LOCK_LEASE_TIME = 5000L;

	private final DistributedLockRepository distributedLockRepository;

	public void executeProtectedDistributedLock(Long accountId, Runnable runnable) {
		final RLock lock = distributedLockRepository.getLock(accountId);

		try {
			attemptAcquireDistributedLock(lock);
			runnable.run();
		} finally {
			releaseLockHeldByThread(lock);
		}
	}

	public void executeProtectedMultiDistributedLock(Long senderAccountId, Long receiverAccountId, Runnable runnable) {
		final RLock lock = distributedLockRepository.getMultiLock(senderAccountId, receiverAccountId);

		try {
			attemptAcquireDistributedLock(lock);
			runnable.run();
		} finally {
			releaseLockHeldByThread(lock);
		}
	}

	private void attemptAcquireDistributedLock(RLock lock) {
		try {
			validateLockAcquired(lock);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(ErrorMessage.LOCK_INTERRUPTED.getMessage(), e);
		}
	}

	private void releaseLockHeldByThread(RLock lock) {
		if (lock != null && lock.isHeldByCurrentThread()) {
			lock.unlock();
		}
	}

	private void validateLockAcquired(RLock rLock) throws InterruptedException {
		if (!rLock.tryLock(MAX_LOCK_WAIT_TIME, MAX_LOCK_LEASE_TIME, TimeUnit.MILLISECONDS)) {
			throw new ConflictException(ErrorMessage.LOCK_ACQUISITION_FAILED);
		}
	}
}
