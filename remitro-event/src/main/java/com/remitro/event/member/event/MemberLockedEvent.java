package com.remitro.event.member.event;

import java.time.LocalDateTime;

import com.remitro.event.member.enums.LockActorType;
import com.remitro.event.member.enums.MemberLockReason;

public record MemberLockedEvent(
	Long memberId,

	Long adminMemberId,

	LockActorType lockActorType,

	MemberLockReason lockReason,

	LocalDateTime occurredAt
) {
}
