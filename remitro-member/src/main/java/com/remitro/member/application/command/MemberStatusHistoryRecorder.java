package com.remitro.member.application.command;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.member.domain.member.enums.ActivityStatus;
import com.remitro.member.domain.status.model.MemberStatusHistory;
import com.remitro.member.domain.status.enums.StatusChangeReason;
import com.remitro.member.domain.status.repository.MemberStatusHistoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberStatusHistoryRecorder {

	private final MemberStatusHistoryRepository memberStatusHistoryRepository;

	private final Clock clock;

	public void record(
		Long memberId,
		ActivityStatus previousStatus,
		ActivityStatus newStatus,
		StatusChangeReason statusChangeReason
	) {
		final MemberStatusHistory memberStatusHistory = MemberStatusHistory.record(
			memberId,
			previousStatus,
			newStatus,
			statusChangeReason,
			LocalDateTime.now(clock)
		);

		memberStatusHistoryRepository.save(memberStatusHistory);
	}
}
