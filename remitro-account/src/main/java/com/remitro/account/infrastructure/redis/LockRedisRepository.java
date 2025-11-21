package com.remitro.account.infrastructure.redis;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import com.remitro.common.infra.error.exception.LockAcquireException;
import com.remitro.common.infra.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LockRedisRepository {

	private final RedissonClient redissonClient;

	public RLock findLock(String key) {
		return redissonClient.getLock(key);
	}

	public boolean tryLock(RLock lock, long waitSeconds, long leaseSeconds) {
		try {
			return lock.tryLock(waitSeconds, leaseSeconds, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new LockAcquireException(ErrorMessage.LOCK_INTERRUPTED);
		}
	}

	public void unLock(RLock lock) {
		if (lock.isHeldByCurrentThread()) {
			lock.unlock();
		}
	}
}
