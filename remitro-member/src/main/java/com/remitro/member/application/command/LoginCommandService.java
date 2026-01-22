package com.remitro.member.application.command;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.BaseException;
import com.remitro.common.exception.ForbiddenException;
import com.remitro.common.exception.UnauthorizedException;
import com.remitro.common.security.Role;
import com.remitro.member.application.command.dto.request.LoginRequest;
import com.remitro.member.application.command.dto.response.TokenResponse;
import com.remitro.member.application.mapper.TokenMapper;
import com.remitro.member.application.support.LoginSecurityRecorder;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.application.support.MemberStatusRecorder;
import com.remitro.member.application.support.TokenIssuanceSupport;
import com.remitro.member.domain.member.enums.ChangeReason;
import com.remitro.member.domain.member.enums.LoginSecurityStatus;
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
	private final MemberStatusRecorder memberStatusRecorder;
	private final LoginSecurityRecorder loginSecurityRecorder;
	private final JwtTokenProvider jwtTokenProvider;
	private final Clock clock;

	@Transactional(noRollbackFor = BaseException.class)
	public TokenResponse login(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
		final LocalDateTime now = LocalDateTime.now(clock);

		final Member member = memberFinder.getMemberByEmail(loginRequest.email());

		validateAndUnlockIfPossible(member, now);
		validateLoginAvailability(member);
		validatePassword(member, loginRequest.password(), now);

		return processLoginSuccess(member, now, httpServletResponse);
	}

	private void validateAndUnlockIfPossible(Member member, LocalDateTime now) {
		final LoginSecurityStatus previousSecurityStatus = member.getLoginSecurityStatus();

		if (!memberLoginPolicy.validateLoginable(member, now)) {
			return;
		}

		loginSecurityRecorder.recordIfChanged(
			member,
			previousSecurityStatus,
			ChangeReason.SYSTEM_UNLOCKED_BY_LOGIN_SUCCESS,
			Role.SYSTEM,
			member.getId()
		);
	}

	private void validateLoginAvailability(Member member) {
		if (member.getLoginSecurityStatus() == LoginSecurityStatus.LOCKED) {
			throw new ForbiddenException(ErrorCode.MEMBER_LOCKED);
		}

		if (member.getMemberStatus() == MemberStatus.SUSPENDED) {
			throw new ForbiddenException(ErrorCode.MEMBER_SUSPENDED);
		}
	}

	private void validatePassword(Member member, String rawPassword, LocalDateTime now) {
		if (memberPasswordPolicy.isPasswordMatch(rawPassword, member.getPassword())) {
			return;
		}

		final LoginSecurityStatus previousSecurityStatus = member.getLoginSecurityStatus();

		memberLoginPolicy.validateFailure(member, now);

		loginSecurityRecorder.recordIfChanged(
			member,
			previousSecurityStatus,
			ChangeReason.SYSTEM_LOCKED_BY_PASSWORD_FAILURE,
			Role.SYSTEM,
			member.getId()
		);

		if (member.getLoginSecurityStatus() == LoginSecurityStatus.LOCKED) {
			throw new ForbiddenException(ErrorCode.MEMBER_LOCKED);
		}
		throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);
	}

	private TokenResponse processLoginSuccess(Member member, LocalDateTime now,
		HttpServletResponse httpServletResponse) {
		releaseDormancyIfNeeded(member, now);

		member.resetFailedCount(now);

		final String accessToken = jwtTokenProvider.issueAccessToken(member.getId(), member.getRole());
		final String refreshToken = jwtTokenProvider.issueRefreshToken(member.getId(), member.getRole());

		tokenIssuanceSupport.process(member.getId(), refreshToken, httpServletResponse);

		return TokenMapper.toLoginResponse(accessToken, refreshToken);
	}

	private void releaseDormancyIfNeeded(Member member, LocalDateTime now) {
		if (!member.isDormant()) {
			return;
		}

		final MemberStatus previousStatus = member.getMemberStatus();

		member.activate(now);

		memberStatusRecorder.recordIfChanged(
			member,
			previousStatus,
			ChangeReason.USER_ACTIVE_BY_DORMANT_RELEASE,
			Role.SYSTEM,
			member.getId()
		);
	}
}
