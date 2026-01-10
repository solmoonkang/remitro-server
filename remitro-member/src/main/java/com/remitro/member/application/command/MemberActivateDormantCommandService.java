package com.remitro.member.application.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.member.enums.ActivityStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.status.enums.StatusChangeReason;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberActivateDormantCommandService {

	private final MemberFinder memberFinder;
	private final MemberStatusHistoryRecorder memberStatusHistoryRecorder;

	public void activate(Long memberId) {
		final Member member = memberFinder.getById(memberId);
		if (!member.isDormant()) {
			return;
		}

		final ActivityStatus previousStatus = member.getActivityStatus();
		member.unlock();

		memberStatusHistoryRecorder.record(
			member.getId(),
			previousStatus,
			member.getActivityStatus(),
			StatusChangeReason.DORMANT_ACTIVATED_BY_LOGIN
		);
	}
}
