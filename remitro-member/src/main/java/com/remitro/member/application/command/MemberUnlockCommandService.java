package com.remitro.member.application.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.event.MemberEventPublisher;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.member.enums.ActivityStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.status.enums.StatusChangeReason;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberUnlockCommandService {

	private final MemberFinder memberFinder;
	private final MemberStatusHistoryRecorder memberStatusHistoryRecorder;

	private final MemberEventPublisher memberEventPublisher;

	public void unlock(Long memberId) {
		final Member member = memberFinder.getById(memberId);

		final ActivityStatus previousStatus = member.getActivityStatus();
		member.unlock();

		memberStatusHistoryRecorder.record(
			member.getId(),
			previousStatus,
			member.getActivityStatus(),
			StatusChangeReason.ADMIN_UNLOCK
		);

		memberEventPublisher.publishMemberUnlocked(member);
	}
}
