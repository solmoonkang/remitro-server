package com.remitro.member.domain.event;

import java.time.LocalDateTime;

public record MemberKycRequestedEvent(
	Long memberId,

	LocalDateTime requestedAt
) {
}
