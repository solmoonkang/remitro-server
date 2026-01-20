package com.remitro.member.application.command;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.command.dto.request.LoginRequest;
import com.remitro.member.application.command.dto.response.TokenResponse;
import com.remitro.member.application.mapper.TokenMapper;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.application.support.TokenIssuanceSupport;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.policy.MemberLoginPolicy;
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
	private final MemberLoginPolicy memberLoginPolicy;
	private final TokenIssuanceSupport tokenIssuanceSupport;
	private final JwtTokenProvider jwtTokenProvider;
	private final Clock clock;

	public TokenResponse login(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
		final Member member = memberFinder.getMemberByEmail(loginRequest.email());

		memberLoginPolicy.validateLoginable(member, LocalDateTime.now(clock));

		if (!memberPasswordPolicy.isPasswordMatch(loginRequest.password(), member.getPassword())) {
			memberLoginPolicy.validateFailure(member, LocalDateTime.now(clock));
		}

		member.resetFailedCount(LocalDateTime.now(clock));

		final String accessToken = jwtTokenProvider.issueAccessToken(member.getId());
		final String refreshToken = jwtTokenProvider.issueRefreshToken(member.getId());

		tokenIssuanceSupport.process(member.getId(), refreshToken, httpServletResponse);

		return TokenMapper.toLoginResponse(accessToken, refreshToken);
	}
}
