package com.remitro.account.infrastructure.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import com.remitro.account.domain.repository.DistributedLockRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedissonLockRepository implements DistributedLockRepository {

	public static final String DISTRIBUTED_LOCK_PREFIX = "redission:lock:";

	private final RedissonClient redissonClient;

	@Override
	public RLock getLock(String key) {
		return redissonClient.getLock(generateLockKey(key));
	}

	@Override
	public RLock getMultiLock(String keyA, String keyB) {
		final RLock lockA = redissonClient.getLock(generateLockKey(keyA));
		final RLock lockB = redissonClient.getLock(generateLockKey(keyB));
		return redissonClient.getMultiLock(lockA, lockB);
	}

	private String generateLockKey(String key) {
		return DISTRIBUTED_LOCK_PREFIX + key;
	}
}
