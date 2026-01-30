package com.remitro.member.infrastructure.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.remitro.support.security.AuthenticatedUser;

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

		final String accessToken = resolveToken(httpServletRequest);
		if (accessToken != null) {
			try {
				final AuthenticatedUser authenticatedUser = jwtTokenProvider.authenticate(accessToken);
				setAuthenticationToContext(authenticatedUser);

			} catch (Exception e) {
				SecurityContextHolder.clearContext();
			}
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	private String resolveToken(HttpServletRequest httpServletRequest) {
		final String bearer = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

		if (bearer != null && bearer.startsWith(BEARER_PREFIX)) {
			return bearer.substring(BEARER_PREFIX.length());
		}

		return null;
	}

	private void setAuthenticationToContext(AuthenticatedUser authenticatedUser) {
		final SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(
			authenticatedUser.role().toAuthority()
		);

		final Authentication authentication = new UsernamePasswordAuthenticationToken(
			authenticatedUser,
			null,
			List.of(grantedAuthority)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
