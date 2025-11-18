package com.remitro.account.domain.constant;

import java.time.format.DateTimeFormatter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountConstant {

	public static final int ACCOUNT_NUMBER_GENERATION_MAX_ATTEMPTS = 5;
	public static final DateTimeFormatter ACCOUNT_NUMBER_GENERATION_FORMAT =
		DateTimeFormatter.ofPattern("yyyyMMddHHmm");

	public static final long MINIMUM_AMOUNT = 0L;
}
