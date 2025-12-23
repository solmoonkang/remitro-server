package com.remitro.member.application.service.admin;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.service.kyc.KycReadService;
import com.remitro.member.application.service.member.MemberEventPublisher;
import com.remitro.member.application.service.member.MemberReadService;
import com.remitro.member.application.validator.KycValidator;
import com.remitro.member.domain.model.KycVerification;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminKycService {

	private final KycValidator kycValidator;
	private final KycReadService kycReadService;
	private final MemberReadService memberReadService;
	private final MemberEventPublisher memberEventPublisher;
	private final Clock clock;

	@Transactional
	public void approveKycByAdmin(Long memberId, Long adminMemberId) {
		final KycVerification kycVerification = kycReadService.findKycVerificationByMemberId(memberId);
		kycValidator.validateApproval(kycVerification);
		kycVerification.completeSuccess();

		final Member member = memberReadService.findMemberById(memberId);
		member.verifyKyc(LocalDateTime.now(clock));

		memberEventPublisher.publishKycVerified(member, adminMemberId, member.getKycVerifiedAt());
	}

	@Transactional
	public void rejectKycByAdmin(Long memberId, Long adminMemberId, String reason) {
		final KycVerification kycVerification = kycReadService.findKycVerificationByMemberId(memberId);
		kycValidator.validateRejection(kycVerification, reason);
		kycVerification.completeReject(reason);

		final Member member = memberReadService.findMemberById(memberId);
		member.rejectKyc();

		memberEventPublisher.publishKycRejected(member, adminMemberId, reason, LocalDateTime.now(clock));
	}
}
