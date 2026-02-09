package com.remitro.member.application.command.status;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.event.common.EventType;
import com.remitro.member.application.mapper.EventMapper;
import com.remitro.member.application.outbox.OutboxEventRecorder;
import com.remitro.member.domain.audit.enums.ChangeReason;
import com.remitro.member.domain.audit.model.StatusHistory;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SuspensionReleaseProcessor {

	private final OutboxEventRecorder outboxEventRecorder;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public StatusHistory processRelease(Member member, LocalDateTime now) {
		final MemberStatus previousStatus = member.getMemberStatus();
		member.unsuspend();

		final StatusHistory statusHistory = StatusHistory.ofSystem(
			member,
			previousStatus,
			ChangeReason.SYSTEM_ACTIVE_BY_SUSPENSION_EXPIRED
		);

		outboxEventRecorder.record(
			EventType.MEMBER_STATUS_CHANGED,
			member.getId(),
			EventMapper.toMemberStatusChangedEvent(member, statusHistory, now)
		);

		return statusHistory;
	}
}
