package com.remitro.member.application.command;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.event.KycEventPublisher;
import com.remitro.member.application.support.KycVerificationFinder;
import com.remitro.member.domain.kyc.model.KycVerification;
import com.remitro.member.presentation.dto.request.KycRejectRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class KycResultCommandService {

	private final KycVerificationFinder kycVerificationFinder;

	private final KycEventPublisher kycEventPublisher;
	private final Clock clock;

	public void approve(Long memberId) {
		final KycVerification kycVerification = kycVerificationFinder.getByMemberId(memberId);

		kycVerification.verify(LocalDateTime.now(clock));

		kycEventPublisher.publishApproved(kycVerification);
	}

	public void reject(Long memberId, KycRejectRequest kycRejectRequest) {
		final KycVerification kycVerification = kycVerificationFinder.getByMemberId(memberId);

		kycVerification.reject(kycRejectRequest.reason(), LocalDateTime.now(clock));

		kycEventPublisher.publishRejected(kycVerification);
	}
}
