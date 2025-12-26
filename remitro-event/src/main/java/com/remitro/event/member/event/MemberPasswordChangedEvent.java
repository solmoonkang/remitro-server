package com.remitro.event.member.event;

import java.time.LocalDateTime;

public record MemberPasswordChangedEvent(
	Long memberId,

	LocalDateTime occurredAt
) {
}
