package com.remitro.event.domain.member;

import java.time.LocalDateTime;

import com.remitro.event.domain.member.enums.LockActorType;
import com.remitro.event.domain.member.enums.MemberLockReason;

public record MemberLockedEvent(
	Long memberId,

	Long adminMemberId,

	LockActorType lockActorType,

	MemberLockReason lockReason,

	LocalDateTime occurredAt
) {
}
