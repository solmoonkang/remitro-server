package com.remitro.account.domain.repository;

import org.redisson.api.RLock;

public interface DistributedLockRepository {

	RLock getLock(Long accountId);

	RLock getMultiLock(Long senderAccountId, Long receiverAccountId);
}
