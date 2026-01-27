package com.remitro.member.application.command.auth.handler;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.common.exception.BaseException;
import com.remitro.common.exception.UnauthorizedException;
import com.remitro.member.application.command.auth.validator.LoginValidator;
import com.remitro.member.application.command.auth.LoginClientInfo;
import com.remitro.member.application.support.LoginHistoryRecorder;
import com.remitro.member.domain.member.model.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginFailureHandler {

	private final LoginHistoryRecorder loginHistoryRecorder;
	private final LoginValidator loginValidator;

	public void handle(
		Member member,
		LoginClientInfo loginClientInfo,
		LocalDateTime now,
		BaseException exception
	) {
		loginHistoryRecorder.processRecordFailure(member.getId(), loginClientInfo, exception.getMessage());

		if (exception instanceof UnauthorizedException) {
			loginValidator.handlePasswordFailure(member, now);
		}
	}
}
