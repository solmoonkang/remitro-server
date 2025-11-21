package com.remitro.account.infrastructure.redis;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import org.springframework.stereotype.Repository;

import com.remitro.common.infra.error.exception.LockAcquireException;
import com.remitro.common.infra.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		try {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		} catch (RedisException e) {
			log.warn("[✅ LOGGER] UNLOCK 처리에 실패했습니다. "
				+ "이미 LEASE TIME 만료로 자동 해제되었거나, REDIS 연결 상태가 불안정한 상황일 수 있습니다."
			);
		}
	}
}
