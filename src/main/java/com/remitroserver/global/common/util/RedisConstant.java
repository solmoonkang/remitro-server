package com.remitroserver.global.common.util;

import java.time.Duration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisConstant {

	public static final String REFRESH_TOKEN_KEY_PREFIX = "REFRESH_TOKEN:";
	public static final String ACCOUNT_FAIL_KEY_PREFIX = "ACCOUNT_FAIL:";
	public static final String ACCOUNT_LOCK_KEY_PREFIX = "ACCOUNT_LOCK:";

	public static final Duration ACCOUNT_LOCK_EXPIRATION = Duration.ofMinutes(5);

	public static final long SECONDS_PER_MINUTE = 60L;
}
