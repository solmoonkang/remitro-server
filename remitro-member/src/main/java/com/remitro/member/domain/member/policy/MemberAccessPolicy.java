package com.remitro.member.domain.member.policy;

import org.springframework.stereotype.Component;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.UnauthorizedException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.member.domain.member.model.Member;

@Component
public class MemberAccessPolicy {

	public void validateAccessible(Member member) {
		if (member.isWithdrawn()) {
			throw new UnauthorizedException(
				ErrorCode.MEMBER_WITHDRAWN, ErrorMessage.MEMBER_WITHDRAWN
			);
		}

		if (member.isDormant()) {
			throw new UnauthorizedException(
				ErrorCode.MEMBER_DORMANT, ErrorMessage.MEMBER_DORMANT
			);
		}
	}
}
