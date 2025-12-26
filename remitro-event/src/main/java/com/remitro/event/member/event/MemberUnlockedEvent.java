package com.remitro.event.member.event;

import java.time.LocalDateTime;

import com.remitro.event.member.enums.UnlockActorType;

public record MemberUnlockedEvent(
	Long memberId,

	Long adminMemberId,

	UnlockActorType unlockActorType,

	LocalDateTime occurredAt
) {
}
