package com.remitro.member.domain.member.policy;

import java.time.LocalDateTime;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.ForbiddenException;
import com.remitro.common.exception.UnauthorizedException;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;

public class MemberLoginPolicy {

	public void validateLoginable(Member member, LocalDateTime now) {
		if (member.getMemberStatus() == MemberStatus.LOCKED) {
			if (member.isUnlockable(now)) {
				member.unlock();
				return;
			}
			throw new ForbiddenException(ErrorCode.MEMBER_LOCKED);
		}
	}

	public void validateFailure(Member member, LocalDateTime now) {
		member.increaseFailedCount(now);

		if (member.getMemberStatus() == MemberStatus.LOCKED) {
			throw new ForbiddenException(ErrorCode.MEMBER_LOCKED);
		}

		throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);
	}
}
