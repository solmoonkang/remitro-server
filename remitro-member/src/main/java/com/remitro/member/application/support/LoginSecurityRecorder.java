package com.remitro.member.application.support;

import org.springframework.stereotype.Component;

import com.remitro.common.security.Role;
import com.remitro.member.domain.member.enums.ChangeReason;
import com.remitro.member.domain.member.enums.LoginSecurityStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.model.StatusHistory;
import com.remitro.member.domain.member.repository.StatusHistoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginSecurityRecorder {

	private final StatusHistoryRepository statusHistoryRepository;

	public void recordIfChanged(
		Member member,
		LoginSecurityStatus previousSecurityStatus,
		ChangeReason changeReason,
		Role changedByRole,
		Long changedById
	) {
		if (previousSecurityStatus == member.getLoginSecurityStatus()) {
			return;
		}

		statusHistoryRepository.save(
			StatusHistory.recordLoginSecurityStatus(
				member,
				previousSecurityStatus,
				member.getLoginSecurityStatus(),
				changeReason,
				changedByRole,
				changedById
			)
		);
	}
}
