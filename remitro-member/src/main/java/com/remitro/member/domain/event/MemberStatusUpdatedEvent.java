package com.remitro.member.domain.event;

import com.remitro.member.domain.enums.ActivityStatus;
import com.remitro.member.domain.enums.KycStatus;

public record MemberStatusUpdatedEvent(
	Long memberId,

	String nickname,

	ActivityStatus activityStatus,

	KycStatus kycStatus,

	boolean isMemberActive
) {
}

