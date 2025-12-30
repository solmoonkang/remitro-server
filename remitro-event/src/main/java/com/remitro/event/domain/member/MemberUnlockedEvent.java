package com.remitro.event.domain.member;

import java.time.LocalDateTime;

import com.remitro.event.domain.member.enums.UnlockActorType;

public record MemberUnlockedEvent(
	Long memberId,

	Long adminMemberId,

	UnlockActorType unlockActorType,

	LocalDateTime occurredAt
) {
}
