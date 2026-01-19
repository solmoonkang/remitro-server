package com.remitro.member.infrastructure.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CookieManager {

	private static final String REFRESH_TOKEN = "refreshToken";

	public void setRefreshTokenCookie(
		HttpServletResponse httpServletResponse,
		String refreshToken,
		long maxAgeSeconds
	) {
		ResponseCookie responseCookie = ResponseCookie.from(REFRESH_TOKEN, refreshToken)
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(maxAgeSeconds)
			.sameSite("Strict")
			.build();

		httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
	}
}
