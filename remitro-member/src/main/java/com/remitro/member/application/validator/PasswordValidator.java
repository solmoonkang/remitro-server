package com.remitro.member.application.validator;

import org.springframework.stereotype.Component;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.BadRequestException;
import com.remitro.common.exception.UnauthorizedException;
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
		if (passwordPolicy.isWrongPassword(currentPassword, encodedPassword)) {
			throw new BadRequestException(ErrorCode.WRONG_PASSWORD);
		}

		if (passwordPolicy.isSamePassword(currentPassword, newPassword)) {
			throw new BadRequestException(ErrorCode.PASSWORD_REUSE_DENIED);
		}

		if (!passwordPolicy.isConfirmationMatched(newPassword, confirmPassword)) {
			throw new BadRequestException(ErrorCode.PASSWORD_CONFIRM_MISMATCH);
		}
	}
}
