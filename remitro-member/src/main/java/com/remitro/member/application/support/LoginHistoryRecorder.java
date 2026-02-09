package com.remitro.member.application.support;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.command.access.LoginClientInfo;
import com.remitro.member.domain.audit.enums.LoginStatus;
import com.remitro.member.domain.audit.model.LoginHistory;
import com.remitro.member.domain.audit.repository.LoginHistoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginHistoryRecorder {

	private final LoginHistoryRepository loginHistoryRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public LoginHistory processRecordSuccess(Long memberId, LoginClientInfo loginClientInfo) {
		return saveHistory(memberId, loginClientInfo, LoginStatus.SUCCESS, null);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public LoginHistory processRecordFailure(Long memberId, LoginClientInfo loginClientInfo, String failureReason) {
		return saveHistory(memberId, loginClientInfo, LoginStatus.FAILURE, failureReason);
	}

	private LoginHistory saveHistory(
		Long memberId,
		LoginClientInfo loginClientInfo,
		LoginStatus loginStatus,
		String failureReason
	) {
		if (loginStatus == LoginStatus.SUCCESS) {
			return loginHistoryRepository.save(
				LoginHistory.ofSuccess(memberId, loginClientInfo.clientIp(), loginClientInfo.userAgent())
			);
		}

		return loginHistoryRepository.save(
			LoginHistory.ofFailure(memberId, loginClientInfo.clientIp(), loginClientInfo.userAgent(), failureReason)
		);
	}
}
