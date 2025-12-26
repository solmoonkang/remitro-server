package com.remitro.event.member.event;

import java.time.LocalDateTime;

import com.remitro.event.member.enums.MemberActivityStatus;
import com.remitro.event.member.enums.MemberKycStatus;

public record MemberCreatedEvent(
	Long memberId,

	String email,

	String nickname,

	MemberActivityStatus activityStatus,

	MemberKycStatus kycStatus,

	LocalDateTime occurredAt
) {
}
