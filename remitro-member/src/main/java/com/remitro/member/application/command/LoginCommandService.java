package com.remitro.member.application.command;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.command.dto.request.LoginRequest;
import com.remitro.member.application.command.dto.response.LoginResponse;
import com.remitro.member.application.mapper.TokenMapper;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.policy.MemberPasswordPolicy;
import com.remitro.member.domain.token.model.RefreshToken;
import com.remitro.member.domain.token.repository.RefreshTokenRepository;
import com.remitro.member.infrastructure.security.JwtProperties;
import com.remitro.member.infrastructure.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginCommandService {

	private final MemberFinder memberFinder;
	private final RefreshTokenRepository refreshTokenRepository;
	private final MemberPasswordPolicy memberPasswordPolicy;
	private final JwtTokenProvider jwtTokenProvider;
	private final JwtProperties jwtProperties;
	private final Clock clock;

	public LoginResponse login(LoginRequest loginRequest) {
		final Member member = memberFinder.getMemberByEmail(loginRequest.email());

		memberPasswordPolicy.validatePasswordMatch(loginRequest.password(), member.getPassword());

		final String accessToken = jwtTokenProvider.issueAccessToken(member.getId());
		final String refreshToken = jwtTokenProvider.issueRefreshToken(member.getId());

		final RefreshToken refreshTokenToStore = RefreshToken.issue(
			member.getId(),
			refreshToken,
			jwtProperties.refreshTokenExpirationTime(),
			LocalDateTime.now(clock)
		);
		refreshTokenRepository.save(refreshTokenToStore);

		return TokenMapper.toLoginResponse(accessToken, refreshToken);
	}
}
