package com.remitro.event.domain.kyc;

public record KycRequestedEvent(
	Long memberId,

	Long kycId
) {
}
