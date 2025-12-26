package com.remitro.member.application.service.admin;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.infrastructure.messaging.MemberEventPublisher;
import com.remitro.member.application.support.KycVerificationFinder;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.application.validator.KycValidator;
import com.remitro.member.domain.model.KycVerification;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminKycCommandService {

	private final KycValidator kycValidator;
	private final MemberFinder memberFinder;
	private final KycVerificationFinder kycVerificationFinder;
	private final Clock clock;
	private final MemberEventPublisher memberEventPublisher;

	@Transactional
	public void approveKycByAdmin(Long memberId, Long adminMemberId) {
		final KycVerification kycVerification = kycVerificationFinder.getLatestVerificationByMemberId(memberId);
		kycValidator.validateApproval(kycVerification);
		kycVerification.completeSuccess(LocalDateTime.now(clock));

		final Member member = memberFinder.getById(memberId);
		member.verifyKyc(LocalDateTime.now(clock));

		memberEventPublisher.publishKycVerified(member, adminMemberId, member.getKycVerifiedAt());
	}

	@Transactional
	public void rejectKycByAdmin(Long memberId, Long adminMemberId, String reason) {
		final KycVerification kycVerification = kycVerificationFinder.getLatestVerificationByMemberId(memberId);
		kycValidator.validateRejection(kycVerification, reason);
		kycVerification.completeReject(reason, LocalDateTime.now(clock));

		final Member member = memberFinder.getById(memberId);
		member.rejectKyc();

		memberEventPublisher.publishKycRejected(member, adminMemberId, reason, LocalDateTime.now(clock));
	}
}
