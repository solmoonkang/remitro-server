package com.remitro.auth.infrastructure.constant;

import java.time.Duration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisConstant {

	public static final String REFRESH_TOKEN_PREFIX = "REFRESH:TOKEN:";
	public static final Long REFRESH_TOKEN_TTL = Duration.ofDays(7).toMillis();
}
