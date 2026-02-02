package com.remitro.account.infrastructure.web;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.remitro.support.security.AuthenticatedUser;
import com.remitro.support.security.Role;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticatedUserFilter extends OncePerRequestFilter {

	private static final String HEADER_MEMBER_ID = "X-MEMBER-ID";
	private static final String HEADER_ROLE = "X-ROLE";

	@Override
	protected void doFilterInternal(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse,
		FilterChain filterChain
	) throws ServletException, IOException {

		final String memberId = httpServletRequest.getHeader(HEADER_MEMBER_ID);
		final String role = httpServletRequest.getHeader(HEADER_ROLE);

		if (memberId != null && role != null) {
			log.info("[✅ LOGGER] 계좌 서비스에서 게이트웨이 인증 헤더가 감지되었습니다. (MEMBER_ID = {}, ROLE = {})",
				memberId, role
			);
			setAuthentication(memberId, role);

		} else {
			log.warn("[✅ LOGGER] 계좌 서비스에서 게이트웨이 인증 헤더가 누락되었습니다. (PATH = {})",
				httpServletRequest.getRequestURI()
			);
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	private void setAuthentication(String memberId, String role) {
		try {
			final AuthenticatedUser authenticatedUser = AuthenticatedUser.of(
				Long.parseLong(memberId),
				Role.valueOf(role)
			);

			final SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_" + role);
			final Authentication authentication = new UsernamePasswordAuthenticationToken(
				authenticatedUser,
				null,
				List.of(simpleGrantedAuthority)
			);
			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (Exception e) {
			log.error("[✅ LOGGER] 계좌 서비스에서 인증 객체 생성에 실패했습니다: {}",
				e.getMessage()
			);
			SecurityContextHolder.clearContext();
		}
	}
}
