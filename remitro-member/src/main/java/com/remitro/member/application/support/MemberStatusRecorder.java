package com.remitro.member.application.support;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.remitro.member.domain.audit.enums.ChangeReason;
import com.remitro.member.domain.audit.model.StatusHistory;
import com.remitro.member.domain.audit.repository.StatusHistoryRepository;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.support.security.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberStatusRecorder {

	private final StatusHistoryRepository statusHistoryRepository;

	public StatusHistory recordSystemAction(Member member, MemberStatus previousStatus, ChangeReason changeReason) {
		if (previousStatus == member.getMemberStatus())
			return null;

		return statusHistoryRepository.save(
			StatusHistory.ofSystem(member, previousStatus, changeReason)
		);
	}

	public StatusHistory recordManualAction(
		Member member,
		MemberStatus previousStatus,
		ChangeReason changeReason,
		Role changedByRole,
		Long changedById
	) {
		if (previousStatus == member.getMemberStatus())
			return null;

		return statusHistoryRepository.save(
			StatusHistory.ofManual(member, previousStatus, changeReason, changedByRole, changedById)
		);
	}

	public void recordAll(Collection<StatusHistory> statusHistories) {
		if (!statusHistories.isEmpty()) {
			statusHistoryRepository.saveAll(statusHistories);
		}
	}
}
