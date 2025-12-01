package com.remitro.account.domain.event;

public record MemberCreatedEvent(
	Long memberId,

	String email,

	String nickname,

	String activityStatus,

	String kycStatus,

	boolean isAccountOpenAllowed
) {
}
