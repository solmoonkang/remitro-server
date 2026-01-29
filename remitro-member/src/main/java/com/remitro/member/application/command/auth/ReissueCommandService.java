package com.remitro.member.application.command.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.support.exception.UnauthorizedException;
import com.remitro.support.security.AuthenticatedUser;
import com.remitro.member.application.command.auth.validator.TokenValidator;
import com.remitro.member.application.command.dto.response.TokenResponse;
import com.remitro.member.application.mapper.TokenMapper;
import com.remitro.member.application.read.auth.TokenFinder;
import com.remitro.member.domain.token.model.RefreshToken;
import com.remitro.member.domain.token.repository.RefreshTokenRepository;
import com.remitro.member.infrastructure.security.JwtTokenProvider;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReissueCommandService {

	private final TokenFinder tokenFinder;
	private final RefreshTokenRepository refreshTokenRepository;
	private final TokenValidator tokenValidator;
	private final TokenIssuanceProcessor tokenIssuanceProcessor;
	private final JwtTokenProvider jwtTokenProvider;

	public TokenResponse reissue(String refreshToken, HttpServletResponse httpServletResponse) {
		final AuthenticatedUser authenticatedUser = jwtTokenProvider.extractUserInfoFromRefreshToken(refreshToken);
		final RefreshToken storedToken = tokenFinder.getRefreshTokenByMemberId(authenticatedUser.memberId());

		try {
			tokenValidator.validateTokenMatch(storedToken, refreshToken);

		} catch (UnauthorizedException e) {
			refreshTokenRepository.deleteByMemberId(authenticatedUser.memberId());
			throw e;
		}

		final String newAccessToken = jwtTokenProvider.issueAccessToken(
			authenticatedUser.memberId(),
			authenticatedUser.role()
		);
		final String newRefreshToken = jwtTokenProvider.issueRefreshToken(
			authenticatedUser.memberId(),
			authenticatedUser.role()
		);

		tokenIssuanceProcessor.issueAndStoreRefreshToken(
			authenticatedUser.memberId(),
			newRefreshToken,
			httpServletResponse
		);

		return TokenMapper.toLoginResponse(newAccessToken, newRefreshToken);
	}
}
