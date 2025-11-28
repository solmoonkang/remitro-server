package com.remitro.member.application.service.kyc;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		kycWriteService.saveKycVerification(memberId);
	}

	@Transactional
	public void completeKyc(Long memberId, UpdateKycStatusRequest updateKycStatusRequest) {
		kycValidator.validateCompletionRequest(updateKycStatusRequest);

		final Member member = memberReadService.findMemberById(memberId);
		final KycVerification kycVerification = kycReadService.findKycVerificationByMemberId(memberId);

		kycWriteService.updateKycVerification(kycVerification, updateKycStatusRequest);
		memberWriteService.updateKycStatus(member, updateKycStatusRequest.kycStatus());
	}
}
