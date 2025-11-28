package com.remitro.member.application.service.kyc;

import org.springframework.stereotype.Service;

import com.remitro.common.error.exception.NotFoundException;
import com.remitro.common.error.model.ErrorMessage;
import com.remitro.member.domain.model.KycVerification;
import com.remitro.member.domain.repository.KycVerificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KycReadService {

	private final KycVerificationRepository kycVerificationRepository;

	public KycVerification findKycVerificationByMemberId(Long memberId) {
		return kycVerificationRepository.findTopByMemberIdOrderByRequestedAtDesc(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.KYC_VERIFICATION_NOT_FOUND));
	}
}
