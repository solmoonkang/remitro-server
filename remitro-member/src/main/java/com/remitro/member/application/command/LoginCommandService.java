package com.remitro.member.application.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.command.dto.request.LoginRequest;
import com.remitro.member.application.command.dto.response.TokenResponse;
import com.remitro.member.application.mapper.TokenMapper;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.application.support.TokenIssuanceSupport;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.policy.MemberPasswordPolicy;
import com.remitro.member.infrastructure.security.JwtTokenProvider;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginCommandService {

	private final MemberFinder memberFinder;
	private final MemberPasswordPolicy memberPasswordPolicy;
	private final TokenIssuanceSupport tokenIssuanceSupport;
	private final JwtTokenProvider jwtTokenProvider;

	public TokenResponse login(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
		final Member member = memberFinder.getMemberByEmail(loginRequest.email());

		memberPasswordPolicy.validatePasswordMatch(loginRequest.password(), member.getPassword());

		final String accessToken = jwtTokenProvider.issueAccessToken(member.getId());
		final String refreshToken = jwtTokenProvider.issueRefreshToken(member.getId());

		tokenIssuanceSupport.process(member.getId(), refreshToken, httpServletResponse);

		return TokenMapper.toLoginResponse(accessToken, refreshToken);
	}
}
