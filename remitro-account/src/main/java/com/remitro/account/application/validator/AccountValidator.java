package com.remitro.account.application.validator;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.exception.ForbiddenException;
import com.remitro.common.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountValidator {

	public static final long MINIMUM_AMOUNT = 0L;

	private final PasswordEncoder passwordEncoder;

	public void validateAccountAccess(Long accountOwnerId, Long loginMemberId) {
		if (!accountOwnerId.equals(loginMemberId)) {
			throw new ForbiddenException(ErrorMessage.ACCOUNT_ACCESS_FORBIDDEN);
		}
	}

	public void validateAccountPasswordMatch(String rawPassword, String savedPassword) {
		if (!passwordEncoder.matches(rawPassword, savedPassword)) {
			throw new BadRequestException(ErrorMessage.INVALID_PASSWORD);
		}
	}

	public void validateAmountPositive(Long amount) {
		if (amount <= MINIMUM_AMOUNT) {
			throw new BadRequestException(ErrorMessage.INVALID_AMOUNT);
		}
	}
}
