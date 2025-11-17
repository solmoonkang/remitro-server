package com.remitro.common.contract.member;

public record MemberStatusChangedEvent(
	Long memberId,

	String nickname,

	ActivityStatus activityStatus,

	KycStatus kycStatus
) {
}
