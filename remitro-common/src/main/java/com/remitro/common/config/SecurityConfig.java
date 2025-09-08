package com.remitro.common.config;

import static org.springframework.http.HttpMethod.*;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.remitro.common.auth.filter.JwtAuthenticationFilter;
import com.remitro.common.auth.provider.JwtProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private static final String[] DEV_TOOLS_ENDPOINTS = {
		"/h2-console/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**"
	};
	private static final String[] AUTHENTICATION_REQUEST_ENDPOINTS = {
		"/api/signup", "/api/login"
	};

	private final JwtProvider jwtProvider;
	private final HandlerExceptionResolver handlerExceptionResolver;

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
			.requestMatchers(DEV_TOOLS_ENDPOINTS)
			.requestMatchers(POST, AUTHENTICATION_REQUEST_ENDPOINTS);
	}

	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		httpSecurity.authorizeHttpRequests(auth -> auth
			.anyRequest().authenticated());

		httpSecurity.addFilterBefore(
			new JwtAuthenticationFilter(jwtProvider, handlerExceptionResolver),
			UsernamePasswordAuthenticationFilter.class
		);

		return httpSecurity.build();
	}
}

