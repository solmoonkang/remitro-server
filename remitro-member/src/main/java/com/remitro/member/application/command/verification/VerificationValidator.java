package com.remitro.member.application.command.verification;

import org.springframework.stereotype.Component;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.ConflictException;
import com.remitro.common.exception.UnauthorizedException;
import com.remitro.member.domain.verification.model.Verification;
import com.remitro.member.domain.verification.policy.VerificationPolicy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VerificationValidator {

	private final VerificationPolicy verificationPolicy;

	public void validateCodeValidity(Verification verification, String verificationCode) {
		if (verificationPolicy.isCodeMismatch(verification, verificationCode)) {
			throw new UnauthorizedException(ErrorCode.VERIFICATION_CODE_MISMATCH);
		}
	}

	public void validateAlreadyConfirmed(Verification verification) {
		if (verification.isVerified()) {
			throw new ConflictException(ErrorCode.VERIFICATION_ALREADY_CONFIRMED);
		}
	}
}
