package com.remitro.member.application.command.account.validator;

import org.springframework.stereotype.Component;

import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.BadRequestException;
import com.remitro.support.exception.UnauthorizedException;
import com.remitro.member.domain.member.policy.PasswordPolicy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordValidator {

	private final PasswordPolicy passwordPolicy;

	public void validatePasswordMatch(String rawPassword, String encodedPassword) {
		if (passwordPolicy.isWrongPassword(rawPassword, encodedPassword)) {
			throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);
		}
	}

	public void validatePasswordChange(
		String encodedPassword,
		String currentPassword,
		String newPassword,
		String confirmPassword
	) {
		if (!passwordPolicy.isConfirmationMatched(newPassword, confirmPassword)) {
			throw new BadRequestException(ErrorCode.PASSWORD_CONFIRM_MISMATCH);
		}

		if (passwordPolicy.isSamePassword(currentPassword, newPassword)) {
			throw new BadRequestException(ErrorCode.PASSWORD_REUSE_DENIED);
		}

		if (passwordPolicy.isWrongPassword(currentPassword, encodedPassword)) {
			throw new BadRequestException(ErrorCode.WRONG_PASSWORD);
		}
	}

	public void validatePasswordConfirm(String newPassword, String confirmPassword) {
		if (!passwordPolicy.isConfirmationMatched(newPassword, confirmPassword)) {
			throw new BadRequestException(ErrorCode.PASSWORD_CONFIRM_MISMATCH);
		}
	}
}
