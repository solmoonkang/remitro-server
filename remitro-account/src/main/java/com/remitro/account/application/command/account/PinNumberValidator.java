package com.remitro.account.application.command.account;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.policy.PinNumberPolicy;
import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.BadRequestException;
import com.remitro.support.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PinNumberValidator {

	private final PinNumberPolicy pinNumberPolicy;

	public void validatePinConfirm(String rawPinNumber, String confirmPinNumber) {
		if (!pinNumberPolicy.isSamePinNumber(rawPinNumber, confirmPinNumber)) {
			throw new BadRequestException(ErrorCode.PASSWORD_CONFIRM_MISMATCH);
		}
	}

	public void validatePinMatch(String rawPassword, String encodedPassword) {
		if (pinNumberPolicy.isWrongPinNumber(rawPassword, encodedPassword)) {
			throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);
		}
	}
}
