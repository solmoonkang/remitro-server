package com.remitro.event.member.event;

import java.time.LocalDateTime;

public record MemberKycRequestedEvent(
	Long memberId,

	LocalDateTime occurredAt
) {
}
