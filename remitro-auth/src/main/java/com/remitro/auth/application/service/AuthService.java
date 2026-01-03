package com.remitro.auth.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.auth.application.dto.request.LoginRequest;
import com.remitro.auth.application.dto.response.TokenResponse;
import com.remitro.auth.domain.policy.TokenPolicy;
import com.remitro.auth.infrastructure.client.MemberCommandClient;
import com.remitro.auth.infrastructure.client.MemberQueryClient;
import com.remitro.auth.infrastructure.persistence.RefreshTokenRepository;
import com.remitro.auth.infrastructure.security.JwtTokenService;
import com.remitro.common.auth.MemberAuthInfo;
import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.UnauthorizedException;
import com.remitro.common.error.message.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final MemberQueryClient memberQueryClient;
	private final MemberCommandClient memberCommandClient;
	private final RefreshTokenRepository refreshTokenRepository;
	private final TokenPolicy tokenPolicy;
	private final JwtTokenService jwtTokenService;

	@Transactional
	public TokenResponse login(String deviceId, LoginRequest loginRequest) {
		final MemberAuthInfo memberAuthInfo = authenticate(loginRequest);

		refreshTokenRepository.revokeByMemberAndDevice(memberAuthInfo.memberId(), deviceId);

		final String accessToken = jwtTokenService.issueAccessToken(memberAuthInfo);
		final String refreshToken = jwtTokenService.issueRefreshToken(memberAuthInfo);

		refreshTokenRepository.save(
			tokenPolicy.createRefreshToken(
				memberAuthInfo.memberId(), refreshToken, deviceId
			)
		);

		memberCommandClient.recordLoginSuccess(memberAuthInfo.memberId());

		return new TokenResponse(accessToken, refreshToken);
	}

	private MemberAuthInfo authenticate(LoginRequest loginRequest) {
		final MemberAuthInfo memberAuthInfo = memberQueryClient.findLoginAuthInfo(
			loginRequest.email()
		);

		if (!passwordEncoder.matches(loginRequest.password(), memberAuthInfo.hashedPassword())) {
			memberCommandClient.recordLoginFailure(memberAuthInfo.memberId());
			throw new UnauthorizedException(
				ErrorCode.PASSWORD_INVALID, ErrorMessage.PASSWORD_INVALID
			);
		}

		return memberAuthInfo;
	}
}
