package com.remitro.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
		return serverHttpSecurity
			.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())

			.csrf(ServerHttpSecurity.CsrfSpec::disable)

			.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
			.formLogin(ServerHttpSecurity.FormLoginSpec::disable)
			.logout(ServerHttpSecurity.LogoutSpec::disable)

			.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.anyExchange().permitAll())

			.build();
	}
}
