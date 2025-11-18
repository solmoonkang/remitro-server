package com.remitro.account.infrastructure.redis;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

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
			throw new RuntimeException(e);
		}
	}

	public void unLock(RLock lock) {
		if (lock.isHeldByCurrentThread()) {
			lock.unlock();
		}
	}
}
