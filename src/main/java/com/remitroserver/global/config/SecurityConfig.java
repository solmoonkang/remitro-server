package com.remitroserver.global.config;

import static com.remitroserver.global.common.util.AuthConstant.*;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.remitroserver.global.auth.filter.JwtAuthenticationFilter;
import com.remitroserver.global.auth.token.JwtProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtProvider jwtProvider;
	private final HandlerExceptionResolver handlerExceptionResolver;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return webSecurity -> webSecurity.ignoring()
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
			.requestMatchers(SECURITY_IGNORED_URLS);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		httpSecurity.authorizeHttpRequests(authorization -> authorization
			.requestMatchers(PUBLIC_API_PATHS).permitAll()
			.anyRequest().authenticated());

		httpSecurity.addFilterBefore(
			new JwtAuthenticationFilter(jwtProvider, handlerExceptionResolver),
			UsernamePasswordAuthenticationFilter.class);

		httpSecurity.exceptionHandling(exceptionHandling -> {
			HttpStatusEntryPoint httpStatusEntryPoint = new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
			exceptionHandling.authenticationEntryPoint(httpStatusEntryPoint);
		});

		return httpSecurity.build();
	}
}
