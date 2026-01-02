package com.remitro.gateway.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeaderNames {

	public static final String MEMBER_ID = "X-Member-Id";
	public static final String MEMBER_ROLE = "X-Member-Role";
	public static final String REQUEST_ID = "X-Request-Id";
}
