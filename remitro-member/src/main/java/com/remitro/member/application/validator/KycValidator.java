package com.remitro.member.application.validator;

import org.springframework.stereotype.Component;

import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.model.ErrorMessage;
import com.remitro.member.application.dto.request.UpdateKycStatusRequest;
import com.remitro.member.domain.enums.KycStatus;
import com.remitro.member.domain.enums.KycVerificationStatus;
import com.remitro.member.domain.repository.KycVerificationRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KycValidator {

	private final KycVerificationRepository kycVerificationRepository;

	public void validateNoPendingKyc(Long memberId) {
		boolean existsPending = kycVerificationRepository.existsByMemberIdAndKycVerificationStatus(
			memberId,
			KycVerificationStatus.PENDING
		);

		if (existsPending) {
			throw new BadRequestException(ErrorMessage.KYC_ALREADY_IN_PROGRESS);
		}
	}

	public void validateCompletionRequest(UpdateKycStatusRequest updateKycStatusRequest) {
		if (updateKycStatusRequest.kycStatus() != KycStatus.VERIFIED) {
			return;
		}

		if (updateKycStatusRequest.reason() == null || updateKycStatusRequest.reason().isBlank()) {
			throw new BadRequestException(ErrorMessage.INVALID_KYC_STATUS_REASON);
		}
	}
}
