package com.remitroserver.global.auth.filter;

import static com.remitroserver.global.common.util.AuthConstant.*;
import static com.remitroserver.global.common.util.JwtConstant.*;
import static com.remitroserver.global.error.model.ErrorMessage.*;

import java.util.Collections;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.remitroserver.api.domain.auth.model.AuthMember;
import com.remitroserver.global.auth.token.JwtProvider;
import com.remitroserver.global.error.exception.UnauthorizedException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Set<String> PERMIT_ALL_PATH_SET = Set.of(PUBLIC_API_PATHS);

	private final JwtProvider jwtProvider;
	private final HandlerExceptionResolver handlerExceptionResolver;

	@Override
	protected void doFilterInternal(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse,
		FilterChain filterChain) {

		try {
			final String permitURI = httpServletRequest.getRequestURI();

			if (PERMIT_ALL_PATH_SET.stream().anyMatch(permitURI::startsWith)) {
				filterChain.doFilter(httpServletRequest, httpServletResponse);
				return;
			}

			final String accessToken = jwtProvider.extractToken(httpServletRequest, AUTHORIZATION_HEADER);

			if (jwtProvider.validateToken(accessToken)) {
				setAuthenticationContext(accessToken);
				filterChain.doFilter(httpServletRequest, httpServletResponse);
				return;
			}

			throw new UnauthorizedException(UNAUTHORIZED_REQUEST_ERROR);
		} catch (Exception e) {
			log.warn("[✅ LOGGER: JWT AUTHENTICATION FILTER] 인증 실패 또는 토큰이 존재하지 않습니다: {}", e.getMessage());
			handlerExceptionResolver.resolveException(httpServletRequest, httpServletResponse, null, e);
		}
	}

	private void setAuthenticationContext(String accessToken) {
		final AuthMember authMember = jwtProvider.extractAuthMemberFromToken(accessToken);
		final Authentication authentication = new UsernamePasswordAuthenticationToken(
			authMember, null, Collections.emptyList());

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
