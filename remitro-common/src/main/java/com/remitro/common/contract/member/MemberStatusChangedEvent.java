package com.remitro.common.contract.member;

public record MemberStatusChangedEvent(
	Long memberId,

	ActivityStatus activityStatus,

	KycStatus kycStatus
) {
}
