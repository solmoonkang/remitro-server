package com.remitro.member.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static final String[] SYSTEM_WHITELIST = {
		"/h2-console/**",
		"/swagger-ui/**",
		"/v3/api-docs/**",
		"/favicon.ico"
	};
	private static final String[] AUTH_WHITELIST = {
		"/api/v1/members/signup",
		"/api/v1/auth/login"
	};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.csrf(AbstractHttpConfigurer::disable)
			.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		httpSecurity
			.authorizeHttpRequests(authorization -> authorization
				.requestMatchers(SYSTEM_WHITELIST).permitAll()
				.requestMatchers(AUTH_WHITELIST).permitAll()
				.anyRequest().authenticated());

		return httpSecurity.build();
	}
}
