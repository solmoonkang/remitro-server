package com.remitro.account.domain.repository;

import org.redisson.api.RLock;

public interface DistributedLockRepository {

	RLock getLock(String key);

	RLock getMultiLock(String keyA, String keyB);
}
