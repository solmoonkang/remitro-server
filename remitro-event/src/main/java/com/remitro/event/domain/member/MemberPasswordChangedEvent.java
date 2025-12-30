package com.remitro.event.domain.member;

import java.time.LocalDateTime;

public record MemberPasswordChangedEvent(
	Long memberId,

	LocalDateTime occurredAt
) {
}
