package com.remitro.event.domain.member;

import java.time.LocalDateTime;

import com.remitro.event.domain.member.enums.MemberActivityStatus;
import com.remitro.event.domain.member.enums.MemberKycStatus;

public record MemberCreatedEvent(
	Long memberId,

	String email,

	String nickname,

	MemberActivityStatus activityStatus,

	MemberKycStatus kycStatus,

	LocalDateTime occurredAt
) {
}
