package com.remitro.member.application.support;

import org.springframework.stereotype.Component;

import com.remitro.common.security.Role;
import com.remitro.member.domain.audit.enums.ChangeReason;
import com.remitro.member.domain.member.enums.LoginSecurityStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.audit.model.StatusHistory;
import com.remitro.member.domain.audit.repository.StatusHistoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginSecurityRecorder {

	private final StatusHistoryRepository statusHistoryRepository;

	public void recordSystemAction(Member member, LoginSecurityStatus previousStatus, ChangeReason changeReason) {
		if (previousStatus == member.getLoginSecurityStatus())
			return;

		statusHistoryRepository.save(
			StatusHistory.ofSecuritySystem(member, previousStatus, changeReason)
		);
	}

	public void recordManualAction(
		Member member,
		LoginSecurityStatus previousStatus,
		ChangeReason changeReason,
		Role changedByRole,
		Long changedById
	) {
		if (previousStatus == member.getLoginSecurityStatus())
			return;

		statusHistoryRepository.save(
			StatusHistory.ofSecurityManual(member, previousStatus, changeReason, changedByRole, changedById)
		);
	}
}
