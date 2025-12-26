package com.remitro.member.application.validator;

import java.time.Clock;

import org.springframework.stereotype.Component;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.member.application.support.KycRetryPolicy;
import com.remitro.member.application.support.KycVerificationFinder;
import com.remitro.member.domain.model.KycVerification;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KycValidator {

	private final KycVerificationFinder kycVerificationFinder;
	private final Clock clock;
	private final KycRetryPolicy kycRetryPolicy;

	public void validateApproval(KycVerification kycVerification) {
		if (kycVerification.isCompleted()) {
			throw new BadRequestException(
				ErrorCode.KYC_ALREADY_COMPLETED, ErrorMessage.KYC_ALREADY_COMPLETED
			);
		}

		if (!kycVerification.isPending()) {
			throw new BadRequestException(
				ErrorCode.KYC_APPROVAL_NOT_ALLOWED, ErrorMessage.KYC_APPROVAL_NOT_ALLOWED
			);
		}
	}

	public void validateRejection(KycVerification kycVerification, String reason) {
		if (kycVerification.isCompleted()) {
			throw new BadRequestException(
				ErrorCode.KYC_ALREADY_COMPLETED, ErrorMessage.KYC_ALREADY_COMPLETED
			);
		}

		if (!kycVerification.isPending()) {
			throw new BadRequestException(
				ErrorCode.KYC_REJECTION_NOT_ALLOWED, ErrorMessage.KYC_REJECTION_NOT_ALLOWED
			);
		}

		if (reason == null || reason.isBlank()) {
			throw new BadRequestException(
				ErrorCode.KYC_REJECTION_REASON_REQUIRED, ErrorMessage.KYC_REJECTION_REASON_REQUIRED
			);
		}
	}

	public void validateKycRequestAllowed(Long memberId) {
		kycVerificationFinder.findLatestVerificationByMemberId(memberId)
			.ifPresent(this::validateRetryPolicy);
	}

	private void validateRetryPolicy(KycVerification latestVerification) {
		if (latestVerification.isPending()) {
			throw new BadRequestException(
				ErrorCode.KYC_ALREADY_IN_PROGRESS, ErrorMessage.KYC_ALREADY_IN_PROGRESS
			);
		}

		if (!kycRetryPolicy.isRetryAllowed(latestVerification, clock)) {
			throw new BadRequestException(
				ErrorCode.KYC_RETRY_NOT_YET_ALLOWED, ErrorMessage.KYC_RETRY_NOT_YET_ALLOWED
			);
		}
	}
}
