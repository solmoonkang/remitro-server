package com.remitro.account.infrastructure.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.remitro.account.infrastructure.web.AuthenticatedUserFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private static final String[] SHOULD_IGNORE_URLS = {
		"/h2-console/**",
		"/swagger-ui/**",
		"/v3/api-docs/**",
		"/favicon.ico"
	};
	private static final String[] AUTHENTICATED_URLS = {
		"/api/v1/accounts/**"
	};

	private final AuthenticatedUserFilter authenticatedUserFilter;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
			.requestMatchers(SHOULD_IGNORE_URLS);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests(authorization -> authorization
				.requestMatchers(AUTHENTICATED_URLS).authenticated()
				.anyRequest().permitAll()
			);

		httpSecurity
			.addFilterBefore(authenticatedUserFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}
}
