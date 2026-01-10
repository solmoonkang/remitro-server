package com.remitro.auth.application.service;

import static com.remitro.common.security.JwtClaims.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.auth.presentation.dto.response.TokenResponse;
import com.remitro.auth.domain.model.RefreshToken;
import com.remitro.auth.domain.policy.TokenPolicy;
import com.remitro.auth.infrastructure.client.MemberQueryClient;
import com.remitro.auth.infrastructure.persistence.RefreshTokenRepository;
import com.remitro.auth.infrastructure.security.JwtTokenService;
import com.remitro.auth.presentation.resolver.AuthorizationHeaderResolver;
import com.remitro.common.auth.MemberAuthInfo;
import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.UnauthorizedException;
import com.remitro.common.error.message.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenReissueService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtTokenService jwtTokenService;
	private final MemberQueryClient memberQueryClient;
	private final TokenPolicy tokenPolicy;
	private final AuthorizationHeaderResolver authorizationHeaderResolver;

	@Transactional
	public TokenResponse reissueTokens(String authorizationHeader) {
		final String refreshToken = authorizationHeaderResolver.resolve(authorizationHeader);
		final RefreshToken savedToken = refreshTokenRepository.findByToken(refreshToken)
			.orElseThrow(() -> new UnauthorizedException(
				ErrorCode.TOKEN_INVALID, ErrorMessage.TOKEN_INVALID
			));

		tokenPolicy.validateTokenReissuable(savedToken);
		refreshTokenRepository.revoke(refreshToken);

		final MemberAuthInfo memberAuthInfo = memberQueryClient.getLoginInfo(
			jwtTokenService.parseClaims(refreshToken).get(MEMBER_EMAIL, String.class)
		);

		final String newAccessToken = jwtTokenService.issueAccessToken(memberAuthInfo);
		final String newRefreshToken = jwtTokenService.issueRefreshToken(memberAuthInfo);

		refreshTokenRepository.save(
			tokenPolicy.createRefreshToken(
				memberAuthInfo.memberId(), newRefreshToken, savedToken.deviceId()
			)
		);

		return new TokenResponse(newAccessToken, newRefreshToken);
	}
}
