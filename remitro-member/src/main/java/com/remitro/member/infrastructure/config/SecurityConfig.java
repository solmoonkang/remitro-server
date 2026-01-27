package com.remitro.member.infrastructure.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.remitro.common.security.Role;
import com.remitro.member.infrastructure.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private static final String[] SHOULD_IGNORE_URLS = {
		"/h2-console/**",
		"/swagger-ui/**",
		"/v3/api-docs/**",
		"/favicon.ico"
	};
	private static final String[] PERMIT_ALL_URLS = {
		"/api/v1/members/signup",
		"/api/v1/members/email-lookup",
		"/api/v1/members/me/password/recovery",

		"/api/v1/auth/login",
		"/api/v1/auth/reissue",

		"/api/v1/verifications/**"
	};
	private static final String[] ROLE_ADMIN_URLS = {
		"/api/v1/admin/**"
	};

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

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
			.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		httpSecurity
			.authorizeHttpRequests(authorization -> authorization
				.requestMatchers(PERMIT_ALL_URLS).permitAll()
				.requestMatchers(ROLE_ADMIN_URLS).hasRole(Role.ADMIN.name())
				.anyRequest().authenticated());

		httpSecurity
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}
}
