package com.remitro.gateway.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GatewayRoutePaths {

	public static final String MEMBER = "/members/**";
	public static final String AUTH = "/auth/**";
	public static final String ACCOUNT = "/accounts/**";
	public static final String TRANSACTION = "/transactions/**";
}
