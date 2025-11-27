package com.remitro.gateway.config;

import static com.remitro.gateway.constant.RouteConstant.*;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()

			.route("member-service", predicateSpec ->
				predicateSpec.path(MEMBER_PATH)
					.uri(MEMBER_SERVICE_URI)
			)

			.route("auth-service", predicateSpec ->
				predicateSpec.path(AUTH_PATH)
					.uri(AUTH_SERVICE_URI)
			)

			.route("account-service", predicateSpec ->
				predicateSpec.path(ACCOUNT_PATH)
					.uri(ACCOUNT_SERVICE_URI)
			)

			.route("transaction-service", predicateSpec ->
				predicateSpec.path(TRANSACTION_PATH)
					.uri(TRANSACTION_SERVICE_URI)
			)

			.build();
	}
}
