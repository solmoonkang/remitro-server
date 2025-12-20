package com.remitro.auth.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.auth.application.dto.request.LoginRequest;
import com.remitro.auth.application.dto.response.TokenResponse;
import com.remitro.auth.domain.repository.TokenRepository;
import com.remitro.auth.domain.token.TokenPolicy;
import com.remitro.auth.infrastructure.client.MemberFeignClient;
import com.remitro.common.auth.MemberAuthInfo;
import com.remitro.common.error.exception.UnauthorizedException;
import com.remitro.common.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final PasswordEncoder passwordEncoder;
	private final MemberFeignClient memberFeignClient;
	private final TokenRepository tokenRepository;
	private final TokenPolicy tokenPolicy;
	private final TokenIssuer tokenIssuer;

	@Transactional
	public TokenResponse loginMember(String deviceId, LoginRequest loginRequest) {
		final MemberAuthInfo memberAuthInfo = authenticate(loginRequest);

		tokenRepository.revokeByMemberAndDevice(memberAuthInfo.memberId(), deviceId);

		final String accessToken = tokenIssuer.issueAccessToken(memberAuthInfo);
		final String refreshToken = tokenIssuer.issueRefreshToken(memberAuthInfo);

		tokenRepository.save(
			tokenPolicy.createRefreshToken(
				memberAuthInfo.memberId(), refreshToken, deviceId
			)
		);

		return new TokenResponse(accessToken, refreshToken);
	}

	private MemberAuthInfo authenticate(LoginRequest loginRequest) {
		final MemberAuthInfo memberAuthInfo = memberFeignClient.findAuthInfo(
			loginRequest.email()
		);

		if (!passwordEncoder.matches(loginRequest.password(), memberAuthInfo.hashedPassword())) {
			throw new UnauthorizedException(ErrorMessage.INVALID_PASSWORD);
		}

		return memberAuthInfo;
	}
}
