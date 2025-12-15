package com.remitro.gateway.routing;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RouteConstant {

	public static final String MEMBER_SERVICE_URI = "lb://remitro-member";
	public static final String AUTH_SERVICE_URI = "lb://remitro-auth";
	public static final String ACCOUNT_SERVICE_URI = "lb://remitro-account";
	public static final String TRANSACTION_SERVICE_URI = "lb://remitro-transaction";

	public static final String MEMBER_PATH = "/members/**";
	public static final String AUTH_PATH = "/auth/**";
	public static final String ACCOUNT_PATH = "/accounts/**";
	public static final String TRANSACTION_PATH = "/transactions/**";

}
