package com.remitro.member.application.usecase.kyc.service;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.member.application.common.support.MemberFinder;
import com.remitro.member.application.common.validator.KycValidator;
import com.remitro.member.domain.kyc.enums.KycStatus;
import com.remitro.member.domain.kyc.model.KycVerification;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.kyc.repository.KycVerificationRepository;
import com.remitro.member.infrastructure.messaging.MemberEventPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberKycRequestService {

	private final KycValidator kycValidator;
	private final MemberFinder memberFinder;
	private final KycVerificationRepository kycVerificationRepository;
	private final MemberEventPublisher memberEventPublisher;
	private final Clock clock;

	@Transactional
	public void requestKyc(Long memberId) {
		final Member member = memberFinder.getById(memberId);

		if (member.getKycStatus() != KycStatus.UNVERIFIED) {
			throw new BadRequestException(
				ErrorCode.KYC_REQUEST_NOT_ALLOWED, ErrorMessage.KYC_REQUEST_NOT_ALLOWED
			);
		}

		kycValidator.validateKycRequestAllowed(memberId);

		final KycVerification kycVerification = KycVerification.create(memberId);
		kycVerificationRepository.save(kycVerification);

		memberEventPublisher.publishKycRequested(member, LocalDateTime.now(clock));
	}
}
