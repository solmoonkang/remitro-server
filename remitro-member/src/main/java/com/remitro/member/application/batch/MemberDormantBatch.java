package com.remitro.member.application.batch;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.command.MemberStatusHistoryRecorder;
import com.remitro.member.domain.member.enums.ActivityStatus;
import com.remitro.member.domain.member.repository.MemberCommandRepository;
import com.remitro.member.domain.status.enums.StatusChangeReason;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberDormantBatch {

	private static final long DORMANT_THRESHOLD_MONTHS = 12;

	private final MemberCommandRepository memberCommandRepository;
	private final MemberStatusHistoryRecorder memberStatusHistoryRecorder;

	private final Clock clock;

	@Transactional
	public void markInactiveMembersAsDormant() {
		final LocalDateTime threshold = LocalDateTime.now(clock).minusMonths(DORMANT_THRESHOLD_MONTHS);

		memberCommandRepository
			.findActiveMembersInactiveSince(ActivityStatus.ACTIVE, threshold)
			.forEach(member -> {
				final ActivityStatus previousStatus = member.getActivityStatus();
				member.dormant();

				memberStatusHistoryRecorder.record(
					member.getId(),
					previousStatus,
					member.getActivityStatus(),
					StatusChangeReason.DORMANT_BY_BATCH
				);
			});
	}
}
