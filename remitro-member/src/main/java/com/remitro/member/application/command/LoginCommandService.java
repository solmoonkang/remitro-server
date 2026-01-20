package com.remitro.member.application.command;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.BaseException;
import com.remitro.common.exception.ForbiddenException;
import com.remitro.common.exception.UnauthorizedException;
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
public class LoginCommandService {

	private final MemberFinder memberFinder;
	private final MemberPasswordPolicy memberPasswordPolicy;
	private final MemberLoginPolicy memberLoginPolicy;
	private final TokenIssuanceSupport tokenIssuanceSupport;
	private final JwtTokenProvider jwtTokenProvider;
	private final Clock clock;

	@Transactional(noRollbackFor = BaseException.class)
	public TokenResponse login(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
		final LocalDateTime now = LocalDateTime.now(clock);
		final Member member = memberFinder.getMemberByEmail(loginRequest.email());

		checkLoginAvailability(member, now);
		checkPassword(member, loginRequest.password(), now);

		return processLoginSuccess(member, now, httpServletResponse);
	}

	private void checkLoginAvailability(Member member, LocalDateTime now) {
		memberLoginPolicy.validateLoginable(member, now);

		if (member.getMemberStatus() == MemberStatus.LOCKED) {
			throw new ForbiddenException(ErrorCode.MEMBER_LOCKED);
		}
	}

	private void checkPassword(Member member, String rawPassword, LocalDateTime now) {
		if (memberPasswordPolicy.isPasswordMatch(rawPassword, member.getPassword())) {
			return;
		}

		memberLoginPolicy.validateFailure(member, now);

		if (member.getMemberStatus() == MemberStatus.LOCKED) {
			throw new ForbiddenException(ErrorCode.MEMBER_LOCKED);
		}

		throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);
	}

	private TokenResponse processLoginSuccess(
		Member member,
		LocalDateTime now,
		HttpServletResponse httpServletResponse
	) {
		if (member.isDormant()) {
			member.activate(now);
		}

		member.resetFailedCount(now);

		final String accessToken = jwtTokenProvider.issueAccessToken(member.getId());
		final String refreshToken = jwtTokenProvider.issueRefreshToken(member.getId());

		tokenIssuanceSupport.process(member.getId(), refreshToken, httpServletResponse);

		return TokenMapper.toLoginResponse(accessToken, refreshToken);
	}
}
