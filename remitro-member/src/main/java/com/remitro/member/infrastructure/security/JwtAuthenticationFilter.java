package com.remitro.member.infrastructure.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse,
		FilterChain filterChain
	) throws ServletException, IOException {
		final String token = resolveToken(httpServletRequest);

		if (token != null) {
			final AuthenticatedUser authenticatedUser = jwtTokenProvider.authenticate(token);
			final Authentication authentication = new UsernamePasswordAuthenticationToken(
				authenticatedUser, null, List.of()
			);

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	private String resolveToken(HttpServletRequest httpServletRequest) {
		String bearer = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
		if (bearer != null && bearer.startsWith(BEARER_PREFIX)) {
			return bearer.substring(7);
		}
		return null;
	}
}
