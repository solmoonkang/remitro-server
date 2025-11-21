package com.remitro.common.infra.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisConstant {

	// IDEMPOTENCY REPOSITORY PREFIX
	public static final String IDEMPOTENCY_PREFIX = "IDEMPOTENCY:PREFIX:";
	public static final String IDEMPOTENCY_MARKER_VALUE = "LOCK";

	// IDEMPOTENCY SERVICE PREFIX
	public static final String OPEN_ACCOUNT_PREFIX = "OPEN:ACCOUNT:";
	public static final String DEPOSIT_PREFIX = "DEPOSIT:";

	// BALANCE CACHE PREFIX
	public static final String BALANCE_CACHE_PREFIX = "ACCOUNT:BALANCE:";

	// IDEMPOTENCY TTL
	public static final long OPEN_ACCOUNT_IDEMPOTENCY_TTL = 60L * 5;
	public static final long BALANCE_CHANGE_IDEMPOTENCY_TTL = 60L;
	public static final long TRANSFER_IDEMPOTENCY_TTL = 60L * 3;

	// BALACE CACHE TTL
	public static final long BALANCE_CACHE_TTL = 30L;
}
