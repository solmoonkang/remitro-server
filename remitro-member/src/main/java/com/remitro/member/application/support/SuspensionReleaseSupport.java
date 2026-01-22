package com.remitro.member.application.support;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.security.Role;
import com.remitro.member.domain.member.enums.ChangeReason;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.model.StatusHistory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SuspensionReleaseSupport {

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public StatusHistory processRelease(Member member) {
		final MemberStatus previousStatus = member.getMemberStatus();

		member.unsuspend();

		return StatusHistory.recordMemberStatus(
			member,
			previousStatus,
			MemberStatus.ACTIVE,
			ChangeReason.SYSTEM_ACTIVE_BY_SUSPENSION_EXPIRED,
			Role.SYSTEM,
			null
		);
	}
}
