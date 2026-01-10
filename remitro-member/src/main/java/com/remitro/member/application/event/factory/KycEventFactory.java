package com.remitro.member.application.event.factory;

import com.remitro.event.domain.kyc.KycApprovedEvent;
import com.remitro.event.domain.kyc.KycRejectedEvent;
import com.remitro.event.domain.kyc.KycRequestedEvent;
import com.remitro.member.domain.kyc.model.KycVerification;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KycEventFactory {

	public static KycRequestedEvent createRequestedEvent(KycVerification kycVerification) {
		return new KycRequestedEvent(
			kycVerification.getMemberId(),
			kycVerification.getId()
		);
	}

	public static KycApprovedEvent createApprovedEvent(KycVerification kycVerification) {
		return new KycApprovedEvent(
			kycVerification.getMemberId(),
			kycVerification.getId()
		);
	}

	public static KycRejectedEvent createRejectedEvent(KycVerification kycVerification) {
		return new KycRejectedEvent(
			kycVerification.getMemberId(),
			kycVerification.getId(),
			kycVerification.getReason()
		);
	}
}
