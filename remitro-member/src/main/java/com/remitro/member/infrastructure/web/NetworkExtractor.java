package com.remitro.member.infrastructure.web;

import org.springframework.stereotype.Component;

import com.remitro.member.application.command.auth.LoginClientInfo;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class NetworkExtractor {

	private static final String X_FORWARDED_FOR = "X-Forwarded-For";
	private static final String USER_AGENT = "User-Agent";
	private static final String UNKNOWN = "unknown";

	public LoginClientInfo extractClientInfo(HttpServletRequest httpServletRequest) {
		final String clientIp = extractClientIp(httpServletRequest);
		final String userAgent = httpServletRequest.getHeader(USER_AGENT);

		return new LoginClientInfo(clientIp, userAgent);
	}

	private String extractClientIp(HttpServletRequest httpServletRequest) {
		String clientIp = httpServletRequest.getHeader(X_FORWARDED_FOR);

		if (clientIp == null || clientIp.isEmpty() || UNKNOWN.equalsIgnoreCase(clientIp)) {
			clientIp = httpServletRequest.getRemoteAddr();
		}

		if (clientIp != null && clientIp.contains(",")) {
			return clientIp.split(",")[0].trim();
		}

		return clientIp;
	}
}
