package com.remitro.common.infrastructure.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisConstant {

	// IDEMPOTENCY REPOSITORY PREFIX
	public static final String IDEMPOTENCY_PREFIX = "IDEMPOTENCY:PREFIX:";
	public static final String IDEMPOTENCY_MARKER_VALUE = "LOCK";

	public static final String OPEN_ACCOUNT_PREFIX = "OPEN:ACCOUNT:";
	public static final String DEPOSIT_PREFIX = "DEPOSIT:";

	public static final long OPEN_ACCOUNT_IDEMPOTENCY_TTL = 60L * 5;
	public static final long BALANCE_CHANGE_IDEMPOTENCY_TTL = 60L;
	public static final long TRANSFER_IDEMPOTENCY_TTL = 60L * 3;

	// BALANCE CACHE PREFIX
	public static final String BALANCE_CACHE_PREFIX = "ACCOUNT:BALANCE:";

	public static final long BALANCE_CACHE_TTL = 30L;

	// DISTRIBUTED LOCK PREFIX
	public static final String ACCOUNT_LOCK_PREFIX = "LOCK:ACCOUNT:";

	public static final long LOCK_ACQUIRE_TIMEOUT_SECONDS = 3L;
	public static final long LOCK_HOLD_DURATION_SECONDS = 15L;
	public static final int LOCK_MAX_RETRY_ATTEMPTS = 5;
	public static final long LOCK_BACKOFF_BASE_MILLISECONDS = 200L;
}
