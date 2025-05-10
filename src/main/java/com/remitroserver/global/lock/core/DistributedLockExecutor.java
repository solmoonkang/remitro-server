package com.remitroserver.global.lock.core;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.remitroserver.global.error.exception.ConflictException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DistributedLockExecutor {

	private final RedissonClient redissonClient;
	private final RetryExecutor retryExecutor;

	public void executeWithMultiLocks(List<String> lockKeys, Runnable task) {
		List<RLock> locks = lockKeys.stream()
			.sorted()
			.map(redissonClient::getLock)
			.toList();

		RLock multiLock = new RedissonMultiLock(locks.toArray(new RLock[0]));
		boolean acquired = false;

		try {
			acquired = multiLock.tryLock(3, 2, TimeUnit.SECONDS);
			if (!acquired)
				throw new ConflictException(ACCOUNT_CONCURRENCY_ERROR);

			retryExecutor.execute(task);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("락 획득 중 인터럽트 발생", e);
		} finally {
			if (acquired && multiLock.isHeldByCurrentThread())
				multiLock.unlock();
		}
	}
}
