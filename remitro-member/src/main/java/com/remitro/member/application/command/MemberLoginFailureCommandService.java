package com.remitro.member.application.command;

import java.time.Clock;
import java.time.LocalDateTime;

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
public class MemberLoginFailureCommandService {

	private final MemberFinder memberFinder;
	private final MemberStatusHistoryRecorder memberStatusHistoryRecorder;

	private final MemberEventPublisher memberEventPublisher;
	private final Clock clock;

	public void handleLoginFailure(Long memberId) {
		final Member member = memberFinder.getById(memberId);

		final ActivityStatus previousStatus = member.getActivityStatus();
		member.increaseLoginFailure(LocalDateTime.now(clock));

		if (previousStatus != ActivityStatus.LOCKED && member.getActivityStatus() == ActivityStatus.LOCKED) {
			memberStatusHistoryRecorder.record(
				member.getId(),
				previousStatus,
				ActivityStatus.LOCKED,
				StatusChangeReason.LOGIN_FAILURE_LOCK
			);

			memberEventPublisher.publishMemberLocked(member);
		}
	}
}
