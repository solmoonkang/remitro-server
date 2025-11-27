package com.remitro.account.application.service.distributedlock;

import static com.remitro.common.infrastructure.util.constant.RedisConstant.*;

import java.util.function.Supplier;

import org.redisson.api.RLock;
import org.redisson.client.RedisException;
import org.springframework.stereotype.Service;

import com.remitro.account.application.service.AccountReadService;
import com.remitro.account.infrastructure.redis.LockRedisRepository;
import com.remitro.common.infrastructure.error.exception.LockAcquireException;
import com.remitro.common.infrastructure.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DistributedLockManager {

	private final AccountReadService accountReadService;
	private final LockRedisRepository lockRedisRepository;

	public <T> T executeWithAccountLock(Long accountId, Supplier<T> action) {
		RLock lock = lockRedisRepository.findLock(ACCOUNT_LOCK_PREFIX + accountId);

		for (int attempt = 1; attempt <= LOCK_MAX_RETRY_ATTEMPTS; attempt++) {
			try {
				if (lockRedisRepository.tryLock(lock, LOCK_ACQUIRE_TIMEOUT_SECONDS, LOCK_HOLD_DURATION_SECONDS)) {
					try {
						return action.get();
					} finally {
						lockRedisRepository.unLock(lock);
					}
				}

				Thread.sleep(LOCK_BACKOFF_BASE_MILLISECONDS * attempt);

			} catch (RedisException e) {
				return runWithDbLockFallback(accountId, action);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new LockAcquireException(ErrorMessage.LOCK_INTERRUPTED);
			}
		}

		throw new LockAcquireException(ErrorMessage.UNABLE_TO_ACQUIRE_LOCK);
	}

	private <T> T runWithDbLockFallback(Long accountId, Supplier<T> action) {
		accountReadService.loadAccountWithLock(accountId);
		return action.get();
	}
}
