package com.remitro.member.application.support;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.remitro.common.security.Role;
import com.remitro.member.domain.member.enums.ChangeReason;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.model.StatusHistory;
import com.remitro.member.domain.member.repository.StatusHistoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberStatusRecorder {

	private final StatusHistoryRepository statusHistoryRepository;

	public void recordIfChanged(
		Member member,
		MemberStatus previousStatus,
		ChangeReason changeReason,
		Role changedByRole,
		Long changedById
	) {
		if (previousStatus == member.getMemberStatus()) {
			return;
		}

		statusHistoryRepository.save(
			StatusHistory.recordMemberStatus(
				member,
				previousStatus,
				member.getMemberStatus(),
				changeReason,
				changedByRole,
				changedById
			)
		);
	}

	public void recordAll(Collection<StatusHistory> statusHistories) {
		if (statusHistories.isEmpty()) {
			return;
		}

		statusHistoryRepository.saveAll(statusHistories);
	}
}
