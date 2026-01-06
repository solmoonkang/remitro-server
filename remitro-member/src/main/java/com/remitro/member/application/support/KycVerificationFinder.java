package com.remitro.member.application.support;

import org.springframework.stereotype.Component;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.NotFoundException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.member.domain.kyc.model.KycVerification;
import com.remitro.member.domain.kyc.repository.KycVerificationQueryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KycVerificationFinder {

	private final KycVerificationQueryRepository kycVerificationQueryRepository;

	public KycVerification getByMemberId(Long memberId) {
		return kycVerificationQueryRepository.findByMemberId(memberId)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.INVALID_REQUEST, ErrorMessage.INVALID_REQUEST
			));
	}
}
