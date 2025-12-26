package com.remitro.auth.application.service;

import static com.remitro.common.security.AuthenticationConstant.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.auth.application.dto.response.TokenResponse;
import com.remitro.auth.application.mapper.TokenMapper;
import com.remitro.auth.domain.model.RefreshToken;
import com.remitro.auth.domain.repository.TokenRepository;
import com.remitro.auth.domain.token.TokenPolicy;
import com.remitro.auth.infrastructure.client.MemberQueryClient;
import com.remitro.auth.infrastructure.security.JwtProvider;
import com.remitro.common.auth.MemberAuthInfo;
import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.UnauthorizedException;
import com.remitro.common.error.message.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenReissueService {

	private final JwtProvider jwtProvider;
	private final TokenRepository tokenRepository;
	private final MemberQueryClient memberQueryClient;
	private final TokenPolicy tokenPolicy;
	private final TokenIssuer tokenIssuer;

	@Transactional
	public TokenResponse reissueTokens(String authorizationHeader) {
		final String refreshToken = jwtProvider.extractToken(authorizationHeader);
		final RefreshToken savedToken = tokenRepository.findByToken(refreshToken)
			.orElseThrow(() -> new UnauthorizedException(
				ErrorCode.TOKEN_INVALID,
				ErrorMessage.TOKEN_INVALID
			));

		tokenPolicy.validateTokenReissuable(savedToken);
		tokenRepository.revoke(refreshToken);

		final MemberAuthInfo memberAuthInfo = memberQueryClient.findReissueAuthInfo(
			jwtProvider.parseClaims(refreshToken).get(CLAIM_MEMBER_EMAIL, String.class)
		);

		final String newAccessToken = tokenIssuer.issueAccessToken(memberAuthInfo);
		final String newRefreshToken = tokenIssuer.issueRefreshToken(memberAuthInfo);

		tokenRepository.save(
			tokenPolicy.createRefreshToken(
				memberAuthInfo.memberId(), newRefreshToken, savedToken.deviceId()
			)
		);

		return TokenMapper.toTokenResponse(newAccessToken, newRefreshToken);
	}
}
