package com.remitro.member.application.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.event.KycEventPublisher;
import com.remitro.member.application.support.KycVerificationFinder;
import com.remitro.member.domain.kyc.model.KycVerification;
import com.remitro.member.domain.kyc.policy.KycRequestPolicy;
import com.remitro.member.domain.kyc.repository.KycVerificationCommandRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class KycRequestCommandService {

	private final KycVerificationFinder kycVerificationFinder;
	private final KycVerificationCommandRepository kycVerificationCommandRepository;

	private final KycRequestPolicy kycRequestPolicy;
	private final KycEventPublisher kycEventPublisher;

	public void requestKyc(Long memberId) {
		final KycVerification kycVerification = kycVerificationFinder.getByMemberId(memberId);

		kycRequestPolicy.validateRequestable(kycVerification);

		kycVerification.startVerification();
		kycVerificationCommandRepository.save(kycVerification);

		kycEventPublisher.publishRequested(kycVerification);
	}
}
