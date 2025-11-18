package com.remitro.account.application.service.distributedlock;

import java.util.function.Supplier;

import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import com.remitro.account.infrastructure.redis.LockRedisRepository;
import com.remitro.common.infra.error.exception.InternalServerException;
import com.remitro.common.infra.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DistributedLockManager {

	public static final String ACCOUNT_LOCK_PREFIX = "LOCK:ACCOUNT:";
	public static final long DEFAULT_WAIT_SECONDS = 3L;
	public static final long DEFAULT_LEASE_SECONDS = 5L;

	private final LockRedisRepository lockRedisRepository;

	public <T> T executeWithAccountLock(Long accountId, Supplier<T> action) {
		RLock lock = lockRedisRepository.findLock(ACCOUNT_LOCK_PREFIX + accountId);

		try {
			if (!lockRedisRepository.tryLock(lock, DEFAULT_WAIT_SECONDS, DEFAULT_LEASE_SECONDS)) {
				throw new InternalServerException(ErrorMessage.UNKNOWN_SERVER_ERROR);
			}
			return action.get();
		} finally {
			lockRedisRepository.unLock(lock);
		}
	}
}
