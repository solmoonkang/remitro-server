package com.remitroserver.global.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountConstant {

	public static final int MAX_FAILED = 5;
	public static final int LOCK_MINUTES = 5;
	public static final long BACKOFF_MS = 100;
}
