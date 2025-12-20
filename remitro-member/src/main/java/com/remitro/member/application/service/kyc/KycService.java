package com.remitro.member.application.service.kyc;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.model.ErrorMessage;
import com.remitro.member.application.dto.request.UpdateKycStatusRequest;
import com.remitro.member.application.service.member.MemberReadService;
import com.remitro.member.application.service.member.MemberWriteService;
import com.remitro.member.application.validator.KycValidator;
import com.remitro.member.domain.enums.KycStatus;
import com.remitro.member.domain.model.KycVerification;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KycService {

	private final MemberReadService memberReadService;
	private final MemberWriteService memberWriteService;
	private final KycReadService kycReadService;
	private final KycWriteService kycWriteService;
	private final KycValidator kycValidator;

	@Transactional
	public void requestKyc(Long memberId) {
		final Member member = memberReadService.findMemberById(memberId);

		if (member.getKycStatus() != KycStatus.UNVERIFIED) {
			throw new BadRequestException(ErrorMessage.KYC_REQUEST_NOT_ALLOWED);
		}

		kycValidator.validateNoPendingKyc(memberId);
		kycWriteService.saveKycVerification(memberId);

		memberWriteService.requestKyc(member);
	}

	@Transactional
	public void completeKyc(Long memberId, UpdateKycStatusRequest updateKycStatusRequest, Long adminMemberId) {
		kycValidator.validateCompletionRequest(updateKycStatusRequest);

		final Member member = memberReadService.findMemberById(memberId);

		if (member.getKycStatus() == KycStatus.VERIFIED) {
			throw new BadRequestException(ErrorMessage.KYC_ALREADY_VERIFIED);
		}

		final KycVerification kycVerification = kycReadService.findKycVerificationByMemberId(memberId);

		kycWriteService.updateKycVerification(kycVerification, updateKycStatusRequest);

		memberWriteService.updateKycStatus(
			member,
			adminMemberId,
			updateKycStatusRequest.kycStatus(),
			updateKycStatusRequest.reason()
		);
	}
}
