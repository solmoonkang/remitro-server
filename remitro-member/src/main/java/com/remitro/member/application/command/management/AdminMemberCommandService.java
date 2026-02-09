package com.remitro.member.application.command.management;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.event.common.EventType;
import com.remitro.member.application.mapper.EventMapper;
import com.remitro.member.application.outbox.OutboxEventRecorder;
import com.remitro.member.application.read.account.MemberFinder;
import com.remitro.member.application.support.MemberStatusRecorder;
import com.remitro.member.domain.audit.enums.ChangeReason;
import com.remitro.member.domain.audit.model.StatusHistory;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.support.security.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminMemberCommandService {

	private final MemberFinder memberFinder;

	private final MemberStatusRecorder memberStatusRecorder;
	private final OutboxEventRecorder outboxEventRecorder;
	private final Clock clock;

	public void suspend(Long adminId, Long memberId) {
		final LocalDateTime now = LocalDateTime.now(clock);

		final Member member = memberFinder.getActiveById(memberId);
		final MemberStatus previousStatus = member.getMemberStatus();

		member.suspend(now, null);

		final StatusHistory statusHistory = memberStatusRecorder.recordManualAction(
			member, previousStatus, ChangeReason.ADMIN_SUSPENDED_BY_REPORT, Role.ADMIN, adminId
		);

		outboxEventRecorder.record(
			EventType.MEMBER_STATUS_CHANGED,
			memberId,
			EventMapper.toMemberStatusChangedEvent(member, statusHistory, now)
		);
	}

	public void unsuspend(Long adminId, Long memberId) {
		final LocalDateTime now = LocalDateTime.now(clock);

		final Member member = memberFinder.getMemberById(memberId);
		final MemberStatus previousStatus = member.getMemberStatus();

		member.unsuspend();

		final StatusHistory statusHistory = memberStatusRecorder.recordManualAction(
			member, previousStatus, ChangeReason.ADMIN_ACTIVE_BY_APPEAL, Role.ADMIN, adminId
		);

		outboxEventRecorder.record(
			EventType.MEMBER_STATUS_CHANGED,
			memberId,
			EventMapper.toMemberStatusChangedEvent(member, statusHistory, now)
		);
	}
}
