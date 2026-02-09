package com.remitro.member.application.command.access.validator;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.ForbiddenException;
import com.remitro.member.application.support.LoginSecurityRecorder;
import com.remitro.member.domain.audit.enums.ChangeReason;
import com.remitro.member.domain.member.enums.LoginSecurityStatus;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.policy.LoginPolicy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginValidator {

	private final LoginPolicy loginPolicy;
	private final LoginSecurityRecorder loginSecurityRecorder;

	public void validateAndUnlockIfEligible(Member member, LocalDateTime now) {
		final LoginSecurityStatus previousSecurityStatus = member.getLoginSecurityStatus();

		if (loginPolicy.unlockIfEligible(member, now)) {
			loginSecurityRecorder.recordSystemAction(
				member, previousSecurityStatus, ChangeReason.SYSTEM_UNLOCKED_BY_LOGIN_SUCCESS
			);
		}
	}

	public void validateLoginEligibility(Member member) {
		if (member.getMemberStatus() == MemberStatus.WITHDRAWN) {
			throw new ForbiddenException(ErrorCode.MEMBER_WITHDRAWN);
		}

		if (member.getMemberStatus() == MemberStatus.SUSPENDED) {
			throw new ForbiddenException(ErrorCode.MEMBER_SUSPENDED);
		}

		if (member.getLoginSecurityStatus() == LoginSecurityStatus.LOCKED) {
			throw new ForbiddenException(ErrorCode.MEMBER_LOCKED);
		}
	}

	public void handlePasswordFailure(Member member, LocalDateTime now) {
		final LoginSecurityStatus previousSecurityStatus = member.getLoginSecurityStatus();

		loginPolicy.applyLoginFailure(member, now);

		loginSecurityRecorder.recordSystemAction(
			member, previousSecurityStatus, ChangeReason.SYSTEM_LOCKED_BY_PASSWORD_FAILURE
		);

		if (member.getLoginSecurityStatus() == LoginSecurityStatus.LOCKED) {
			throw new ForbiddenException(ErrorCode.MEMBER_LOCKED);
		}
	}
}
