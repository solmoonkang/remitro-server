package com.remitro.member.application.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.exception.UnauthorizedException;
import com.remitro.member.application.command.dto.response.TokenResponse;
import com.remitro.member.application.mapper.TokenMapper;
import com.remitro.member.application.support.TokenFinder;
import com.remitro.member.application.support.TokenIssuanceSupport;
import com.remitro.member.domain.token.model.RefreshToken;
import com.remitro.member.domain.token.policy.RefreshTokenPolicy;
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
	private final RefreshTokenPolicy refreshTokenPolicy;
	private final TokenIssuanceSupport tokenIssuanceSupport;
	private final JwtTokenProvider jwtTokenProvider;

	public TokenResponse reissue(String refreshToken, HttpServletResponse httpServletResponse) {
		final Long memberId = jwtTokenProvider.extractMemberId(refreshToken);
		final RefreshToken storedToken = tokenFinder.getRefreshTokenByMemberId(memberId);

		try {
			refreshTokenPolicy.validateMatch(storedToken, refreshToken);

		} catch (UnauthorizedException e) {
			refreshTokenRepository.deleteByMemberId(memberId);
			throw e;
		}

		final String newAccessToken = jwtTokenProvider.issueAccessToken(memberId);
		final String newRefreshToken = jwtTokenProvider.issueRefreshToken(memberId);
		tokenIssuanceSupport.process(memberId, newRefreshToken, httpServletResponse);

		return TokenMapper.toLoginResponse(newAccessToken, newRefreshToken);
	}
}
