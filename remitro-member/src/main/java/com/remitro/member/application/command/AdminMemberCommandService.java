package com.remitro.member.application.command;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.security.Role;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.application.support.MemberStatusRecorder;
import com.remitro.member.domain.member.enums.ChangeReason;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminMemberCommandService {

	private final MemberFinder memberFinder;
	private final MemberStatusRecorder memberStatusRecorder;
	private final Clock clock;

	public void suspend(Long adminId, Long memberId) {
		final LocalDateTime now = LocalDateTime.now(clock);

		final Member member = memberFinder.getMemberById(memberId);
		final MemberStatus previousStatus = member.getMemberStatus();

		member.suspend(now);

		memberStatusRecorder.recordIfChanged(
			member,
			previousStatus,
			ChangeReason.ADMIN_SUSPENDED_BY_REPORT,
			Role.ADMIN,
			adminId
		);
	}

	public void unsuspend(Long adminId, Long memberId) {
		final Member member = memberFinder.getMemberById(memberId);
		final MemberStatus previousStatus = member.getMemberStatus();

		member.unsuspend();

		memberStatusRecorder.recordIfChanged(
			member,
			previousStatus,
			ChangeReason.ADMIN_ACTIVE_BY_APPEAL,
			Role.ADMIN,
			adminId
		);
	}
}
