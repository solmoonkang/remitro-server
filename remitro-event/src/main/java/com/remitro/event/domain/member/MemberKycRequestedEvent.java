package com.remitro.event.domain.member;

import java.time.LocalDateTime;

public record MemberKycRequestedEvent(
	Long memberId,

	LocalDateTime occurredAt
) {
}
