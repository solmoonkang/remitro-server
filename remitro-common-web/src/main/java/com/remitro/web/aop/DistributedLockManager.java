package com.remitro.web.aop;

public interface DistributedLockManager {

	void acquireLock(Long targetId, long waitTime, long leaseTime);

	void releaseLock(Long targetId);
}
