package com.remitro.account.infrastructure.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import com.remitro.account.domain.repository.DistributedLockRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedissonLockRepository implements DistributedLockRepository {

	public static final String DISTRIBUTED_LOCK_PREFIX = "REDISSON:LOCK:";

	private final RedissonClient redissonClient;

	@Override
	public RLock getLock(Long accountId) {
		return redissonClient.getLock(generateLockKey(accountId));
	}

	@Override
	public RLock getMultiLock(Long senderAccountId, Long receiverAccountId) {
		final RLock lockA = redissonClient.getLock(generateLockKey(senderAccountId));
		final RLock lockB = redissonClient.getLock(generateLockKey(receiverAccountId));
		return redissonClient.getMultiLock(lockA, lockB);
	}

	private String generateLockKey(Long accountId) {
		return DISTRIBUTED_LOCK_PREFIX + accountId;
	}
}
