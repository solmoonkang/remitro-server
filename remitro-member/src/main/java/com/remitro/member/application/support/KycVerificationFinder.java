package com.remitro.member.application.support;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.NotFoundException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.member.domain.enums.KycVerificationStatus;
import com.remitro.member.domain.model.KycVerification;
import com.remitro.member.domain.repository.KycVerificationRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KycVerificationFinder {

	private final KycVerificationRepository kycVerificationRepository;

	public KycVerification getLatestVerificationByMemberId(Long memberId) {
		return kycVerificationRepository.findTopByMemberIdOrderByRequestedAtDesc(memberId)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.KYC_VERIFICATION_NOT_FOUND, ErrorMessage.KYC_VERIFICATION_NOT_FOUND)
			);
	}

	public Optional<KycVerification> findLatestVerificationByMemberId(Long memberId) {
		return kycVerificationRepository.findTopByMemberIdOrderByRequestedAtDesc(memberId);
	}

	public List<KycVerification> findAllPending() {
		return kycVerificationRepository.findAllByKycVerificationStatusOrderByRequestedAtAsc(
			KycVerificationStatus.PENDING
		);
	}
}
