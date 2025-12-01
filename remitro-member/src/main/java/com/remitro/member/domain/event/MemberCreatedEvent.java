package com.remitro.member.domain.event;

public record MemberCreatedEvent(
	Long memberId,

	String email,

	String nickname,

	String activityStatus,

	String kycStatus,

	boolean isMemberActive
) {
}
