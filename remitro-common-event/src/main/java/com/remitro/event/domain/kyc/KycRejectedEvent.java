package com.remitro.event.domain.kyc;

public record KycRejectedEvent(
	Long memberId,

	Long kycId,

	String reason
) {
}
