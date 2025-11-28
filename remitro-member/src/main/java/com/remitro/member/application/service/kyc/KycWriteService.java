package com.remitro.member.application.service.kyc;

import org.springframework.stereotype.Service;

import com.remitro.member.application.dto.request.UpdateKycStatusRequest;
import com.remitro.member.domain.model.KycVerification;
import com.remitro.member.domain.repository.KycVerificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KycWriteService {

	private final KycVerificationRepository kycVerificationRepository;

	public void saveKycVerification(Long memberId) {
		final KycVerification kycVerification = KycVerification.create(memberId);
		kycVerificationRepository.save(kycVerification);
	}

	public void updateKycVerification(KycVerification kycVerification, UpdateKycStatusRequest updateKycStatusRequest) {
		switch (updateKycStatusRequest.kycStatus()) {
			case VERIFIED -> kycVerification.completeSuccess();
			case REJECTED, PENDING -> kycVerification.completeReject(updateKycStatusRequest.reason());
		}
	}
}
