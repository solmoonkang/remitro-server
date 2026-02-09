package com.remitro.member.application.command.access.handler;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.support.security.Role;
import com.remitro.member.application.command.access.LoginClientInfo;
import com.remitro.member.application.dto.response.TokenResponse;
import com.remitro.member.application.mapper.TokenMapper;
import com.remitro.member.application.command.access.TokenIssuanceProcessor;
import com.remitro.member.application.support.LoginHistoryRecorder;
import com.remitro.member.application.support.MemberStatusRecorder;
import com.remitro.member.domain.audit.enums.ChangeReason;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.infrastructure.security.JwtTokenProvider;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler {

	private final LoginHistoryRecorder loginHistoryRecorder;
	private final MemberStatusRecorder memberStatusRecorder;
	private final TokenIssuanceProcessor tokenIssuanceProcessor;
	private final JwtTokenProvider jwtTokenProvider;

	public TokenResponse handle(
		Member member,
		LoginClientInfo loginClientInfo,
		LocalDateTime now,
		HttpServletResponse httpServletResponse
	) {
		loginHistoryRecorder.processRecordSuccess(member.getId(), loginClientInfo);

		releaseDormancyIfNeeded(member, now);
		member.resetFailedCount(now);

		final String accessToken = jwtTokenProvider.issueAccessToken(
			member.getId(), member.getRole()
		);
		final String refreshToken = jwtTokenProvider.issueRefreshToken(
			member.getId(), member.getRole()
		);

		tokenIssuanceProcessor.issueAndStoreRefreshToken(
			member.getId(), refreshToken, httpServletResponse
		);

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
