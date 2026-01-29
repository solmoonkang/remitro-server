package com.remitro.member.application.read.verification;

import org.springframework.stereotype.Component;

import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.NotFoundException;
import com.remitro.support.exception.UnauthorizedException;
import com.remitro.member.domain.verification.model.Verification;
import com.remitro.member.domain.verification.repository.VerificationRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VerificationFinder {

	private final VerificationRepository verificationRepository;

	public Verification getLatestPendingCode(String email) {
		return verificationRepository.findByEmail(email)
			.filter(verification -> !verification.isVerified())
			.orElseThrow(() -> new NotFoundException(ErrorCode.VERIFICATION_NOT_FOUND));
	}

	public Verification getVerifiedToken(String email) {
		return verificationRepository.findByEmail(email)
			.orElseThrow(() -> new UnauthorizedException(ErrorCode.VERIFICATION_NOT_FOUND));
	}
}
