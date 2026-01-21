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
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.application.support.StatusHistoryRecorder;
import com.remitro.member.application.support.TokenIssuanceSupport;
import com.remitro.member.domain.member.enums.ChangeReason;
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
	private final StatusHistoryRecorder statusHistoryRecorder;
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
		final MemberStatus previousStatus = member.getMemberStatus();

		memberLoginPolicy.validateLoginable(member, now);

		statusHistoryRecorder.recordIfChanged(member, previousStatus, ChangeReason.SYSTEM_UNLOCKED_BY_LOGIN_SUCCESS,
			Role.SYSTEM, member.getId());
	}

	private void validateLoginAvailability(Member member) {
		if (member.getMemberStatus() == MemberStatus.LOCKED) {
			throw new ForbiddenException(ErrorCode.MEMBER_LOCKED);
		}
	}

	private void validatePassword(Member member, String rawPassword, LocalDateTime now) {
		if (memberPasswordPolicy.isPasswordMatch(rawPassword, member.getPassword())) {
			return;
		}

		final MemberStatus previousStatus = member.getMemberStatus();

		memberLoginPolicy.validateFailure(member, now);

		statusHistoryRecorder.recordIfChanged(
			member,
			previousStatus,
			ChangeReason.SYSTEM_LOCKED_BY_PASSWORD_FAILURE,
			Role.SYSTEM,
			member.getId()
		);

		if (member.getMemberStatus() == MemberStatus.LOCKED) {
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

		statusHistoryRecorder.recordIfChanged(member, previousStatus, ChangeReason.USER_ACTIVE_BY_DORMANT_RELEASE,
			Role.SYSTEM, member.getId());
	}
}
