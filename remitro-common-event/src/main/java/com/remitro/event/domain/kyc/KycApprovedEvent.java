package com.remitro.event.domain.kyc;

public record KycApprovedEvent(
	Long memberId,

	Long kycId
) {
}
