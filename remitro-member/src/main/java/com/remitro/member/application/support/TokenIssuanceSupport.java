package com.remitro.member.application.support;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.member.domain.token.model.RefreshToken;
import com.remitro.member.domain.token.repository.RefreshTokenRepository;
import com.remitro.member.infrastructure.security.JwtTokenProvider;
import com.remitro.member.infrastructure.web.CookieManager;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenIssuanceSupport {

	private final RefreshTokenRepository refreshTokenRepository;
	private final CookieManager cookieManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final Clock clock;

	public void process(Long memberId, String refreshToken, HttpServletResponse httpServletResponse) {
		final long expirationTime = jwtTokenProvider.getRefreshTokenExpirationTime();

		final RefreshToken tokenToStore = RefreshToken.issue(
			memberId,
			refreshToken,
			expirationTime,
			LocalDateTime.now(clock)
		);
		refreshTokenRepository.save(tokenToStore);

		cookieManager.setRefreshTokenCookie(httpServletResponse, refreshToken, expirationTime / 1000);
	}
}
