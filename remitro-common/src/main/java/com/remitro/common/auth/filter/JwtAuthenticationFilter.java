package com.remitro.common.auth.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.remitro.common.auth.model.AuthMember;
import com.remitro.common.auth.provider.JwtProvider;
import com.remitro.common.error.exception.UnauthorizedException;
import com.remitro.common.error.model.ErrorMessage;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final HandlerExceptionResolver handlerExceptionResolver;

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest httpServletRequest,
		@NonNull HttpServletResponse httpServletResponse,
		@NonNull FilterChain filterChain) {

		try {
			final String accessToken = jwtProvider.extractToken(httpServletRequest);
			if (!StringUtils.hasText(accessToken) && !jwtProvider.validateToken(accessToken)) {
				throw new UnauthorizedException(ErrorMessage.TOKEN_EXPIRED);
			}

			setAuthentication(accessToken);
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
			handlerExceptionResolver.resolveException(httpServletRequest, httpServletResponse, null, e);
		}
	}

	private void setAuthentication(String accessToken) {
		final AuthMember authMember = jwtProvider.extractAuthMemberFromToken(accessToken);
		final Authentication authentication = new UsernamePasswordAuthenticationToken(authMember, null, null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
