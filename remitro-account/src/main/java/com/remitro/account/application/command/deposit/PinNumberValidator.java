package com.remitro.account.application.command.deposit;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.policy.PinNumberPolicy;
import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PinNumberValidator {

	private final PinNumberPolicy pinNumberPolicy;

	public void validateConfirm(String rawPinNumber, String confirmPinNumber) {
		if (!pinNumberPolicy.isSamePinNumber(rawPinNumber, confirmPinNumber)) {
			throw new BadRequestException(ErrorCode.PASSWORD_CONFIRM_MISMATCH);
		}
	}
}
