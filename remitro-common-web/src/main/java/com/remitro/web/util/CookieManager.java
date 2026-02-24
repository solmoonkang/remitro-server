package com.remitro.web.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import jakarta.servlet.http.HttpServletResponse;

public class CookieManager {

	private static final String REFRESH_TOKEN = "refreshToken";

	public void setRefreshTokenCookie(
		HttpServletResponse httpServletResponse,
		String refreshToken,
		long maxAgeSeconds
	) {
		ResponseCookie responseCookie = ResponseCookie.from(REFRESH_TOKEN, refreshToken)
			.httpOnly(true)
			.secure(false)        // .secure(true) -> 로컬 HTTP 테스트를 위해 false로 변경
			.path("/")
			.maxAge(maxAgeSeconds)
			.sameSite("Lax")    // .sameSite("Strict") -> 로컬에서 클라이언트/서버의 도메인이 다르면 Lax가 편함
			.build();

		httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
	}

	public void deleteRefreshTokenCookie(HttpServletResponse httpServletResponse) {
		ResponseCookie responseCookie = ResponseCookie.from(REFRESH_TOKEN, "")
			.maxAge(0)
			.path("/")
			.build();

		httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
	}
}
