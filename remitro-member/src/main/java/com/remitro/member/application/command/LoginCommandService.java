package com.remitro.member.application.command;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.exception.BaseException;
import com.remitro.common.exception.UnauthorizedException;
import com.remitro.common.security.Role;
import com.remitro.member.application.command.dto.request.LoginRequest;
import com.remitro.member.application.command.dto.response.TokenResponse;
import com.remitro.member.application.mapper.TokenMapper;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.application.support.MemberStatusRecorder;
import com.remitro.member.application.support.TokenIssuanceSupport;
import com.remitro.member.application.validator.LoginValidator;
import com.remitro.member.application.validator.PasswordValidator;
import com.remitro.member.domain.history.enums.ChangeReason;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.infrastructure.security.JwtTokenProvider;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginCommandService {

	private final MemberFinder memberFinder;
	private final LoginValidator loginValidator;
	private final PasswordValidator passwordValidator;
	private final TokenIssuanceSupport tokenIssuanceSupport;
	private final MemberStatusRecorder memberStatusRecorder;
	private final JwtTokenProvider jwtTokenProvider;
	private final Clock clock;

	@Transactional(noRollbackFor = BaseException.class)
	public TokenResponse login(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
		final LocalDateTime now = LocalDateTime.now(clock);

		final Member member = memberFinder.getMemberByEmail(loginRequest.email());

		loginValidator.validateAndUnlockIfEligible(member, now);
		loginValidator.validateLoginEligibility(member);

		try {
			passwordValidator.validatePasswordMatch(loginRequest.password(), member.getPasswordHash());

		} catch (UnauthorizedException e) {
			loginValidator.handlePasswordFailure(member, now);
			throw e;
		}

		return processLoginSuccess(member, now, httpServletResponse);
	}

	private TokenResponse processLoginSuccess(
		Member member,
		LocalDateTime now,
		HttpServletResponse httpServletResponse
	) {
		releaseDormancyIfNeeded(member, now);
		member.resetFailedCount(now);

		final String accessToken = jwtTokenProvider.issueAccessToken(member.getId(), member.getRole());
		final String refreshToken = jwtTokenProvider.issueRefreshToken(member.getId(), member.getRole());
		tokenIssuanceSupport.issueAndStoreRefreshToken(member.getId(), refreshToken, httpServletResponse);

		return TokenMapper.toLoginResponse(accessToken, refreshToken);
	}

	private void releaseDormancyIfNeeded(Member member, LocalDateTime now) {
		if (!member.isDormant()) {
			return;
		}

		final MemberStatus previousStatus = member.getMemberStatus();
		member.activate(now);

		memberStatusRecorder.recordManualAction(
			member, previousStatus, ChangeReason.USER_ACTIVE_BY_DORMANT_RELEASE, Role.USER, member.getId()
		);
	}
}
