package com.remitro.common.contract.member;

public record MemberCreatedEvent(
	Long memberId,

	String nickname,

	ActivityStatus activityStatus
) {
}
