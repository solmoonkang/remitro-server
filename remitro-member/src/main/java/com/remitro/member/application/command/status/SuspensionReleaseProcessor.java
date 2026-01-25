package com.remitro.member.application.command.status;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.domain.audit.enums.ChangeReason;
import com.remitro.member.domain.audit.model.StatusHistory;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SuspensionReleaseProcessor {

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public StatusHistory processRelease(Member member) {
		final MemberStatus previousStatus = member.getMemberStatus();
		member.unsuspend();

		return StatusHistory.ofSystem(member, previousStatus, ChangeReason.SYSTEM_ACTIVE_BY_SUSPENSION_EXPIRED);
	}
}
