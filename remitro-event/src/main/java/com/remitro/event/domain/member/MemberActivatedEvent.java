package com.remitro.event.domain.member;

import java.time.LocalDateTime;

public record MemberActivatedEvent(
	Long memberId,

	LocalDateTime occurredAt
) {
}
