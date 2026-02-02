package com.remitro.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.remitro.gateway.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RouteConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()

			.route("member-service", routeSpec -> routeSpec
				.path("/api/v1/members/**", "/api/v1/auth/**")
				.filters(filterSpec -> filterSpec.filter(jwtAuthenticationFilter))
				.uri("lb://remitro-member")
			)

			.route("account-service", routeSpec -> routeSpec
				.path("/api/v1/accounts/**")
				.filters(filterSpec -> filterSpec.filter(jwtAuthenticationFilter))
				.uri("lb://remitro-account")
			)

			.route("transaction-service", routeSpec -> routeSpec
				.path("/api/v1/transactions/**")
				.filters(filterSpec -> filterSpec.filter(jwtAuthenticationFilter))
				.uri("lb://remitro-transaction")
			)

			.build();
	}
}
