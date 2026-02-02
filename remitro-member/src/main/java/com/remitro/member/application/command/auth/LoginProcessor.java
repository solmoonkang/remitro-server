package com.remitro.member.application.command.auth;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.support.exception.BaseException;
import com.remitro.member.application.command.auth.validator.LoginValidator;
import com.remitro.member.application.command.dto.response.TokenResponse;
import com.remitro.member.application.command.auth.handler.LoginFailureHandler;
import com.remitro.member.application.command.auth.handler.LoginSuccessHandler;
import com.remitro.member.application.command.account.validator.PasswordValidator;
import com.remitro.member.domain.member.model.Member;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginProcessor {

	private final LoginValidator loginValidator;
	private final PasswordValidator passwordValidator;
	private final LoginFailureHandler loginFailureHandler;
	private final LoginSuccessHandler loginSuccessHandler;

	public TokenResponse login(
		Member member,
		String rawPassword,
		LoginClientInfo loginClientInfo,
		LocalDateTime now,
		HttpServletResponse httpServletResponse
	) {
		try {
			loginValidator.validateAndUnlockIfEligible(member, now);
			loginValidator.validateLoginEligibility(member);
			passwordValidator.validatePasswordMatch(rawPassword, member.getPasswordHash());

			return loginSuccessHandler.handle(member, loginClientInfo, now, httpServletResponse);

		} catch (BaseException e) {
			loginFailureHandler.handle(member, loginClientInfo, now, e);
			throw e;
		}
	}
}
