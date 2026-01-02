package com.remitro.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.remitro.gateway.support.GatewayRoutePaths;
import com.remitro.gateway.support.GatewayRouteUris;

@Configuration
public class GatewayRouteConfig {

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
			.route(
				"member-service",
				predicate -> predicate.path(GatewayRoutePaths.MEMBER).uri(GatewayRouteUris.MEMBER)
			)
			.route(
				"auth-service",
				predicate -> predicate.path(GatewayRoutePaths.AUTH).uri(GatewayRouteUris.AUTH)
			)
			.route(
				"account-service",
				predicate -> predicate.path(GatewayRoutePaths.ACCOUNT).uri(GatewayRouteUris.ACCOUNT)
			)
			.route(
				"transaction-service",
				predicate -> predicate.path(GatewayRoutePaths.TRANSACTION).uri(GatewayRouteUris.TRANSACTION)
			)
			.build();
	}
}
