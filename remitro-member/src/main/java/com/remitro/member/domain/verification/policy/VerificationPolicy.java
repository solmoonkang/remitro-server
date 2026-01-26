package com.remitro.member.domain.verification.policy;

import org.springframework.stereotype.Component;

import com.remitro.member.domain.verification.model.Verification;

@Component
public class VerificationPolicy {

	public boolean isCodeMismatch(Verification verification, String verificationCode) {
		return !verification.getVerificationCode().equals(verificationCode);
	}
}
