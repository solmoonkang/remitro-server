package com.remitro.auth.infrastructure.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisKeys {

	public static final String REFRESH_TOKEN_PREFIX = "REFRESH:TOKEN:";
}
