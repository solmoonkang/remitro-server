package com.remitro.account.domain.event;

public record MemberStatusUpdatedEvent(
	Long memberId,

	String nickname,

	String activityStatus,

	String kycStatus,

	boolean isAccountOpenAllowed
) {
}
