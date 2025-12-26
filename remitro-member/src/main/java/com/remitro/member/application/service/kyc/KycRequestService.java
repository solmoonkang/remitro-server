package com.remitro.member.application.service.kyc;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.member.application.service.member.MemberReadService;
import com.remitro.member.application.validator.KycValidator;
import com.remitro.member.domain.enums.KycStatus;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KycRequestService {

	private final MemberReadService memberReadService;
	private final KycWriteService kycWriteService;
	private final KycValidator kycValidator;

	@Transactional
	public void requestKyc(Long memberId) {
		final Member member = memberReadService.findMemberById(memberId);

		if (member.getKycStatus() != KycStatus.UNVERIFIED) {
			throw new BadRequestException(
				ErrorCode.KYC_REQUEST_NOT_ALLOWED,
				ErrorMessage.KYC_REQUEST_NOT_ALLOWED
			);
		}

		kycValidator.validateKycRequestAllowed(memberId);
		kycWriteService.saveKycVerification(memberId);

	}
}
