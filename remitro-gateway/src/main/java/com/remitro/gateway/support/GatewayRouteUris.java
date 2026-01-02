package com.remitro.gateway.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GatewayRouteUris {

	public static final String MEMBER = "lb://remitro-member";
	public static final String AUTH = "lb://remitro-auth";
	public static final String ACCOUNT = "lb://remitro-account";
	public static final String TRANSACTION = "lb://remitro-transaction";
}
