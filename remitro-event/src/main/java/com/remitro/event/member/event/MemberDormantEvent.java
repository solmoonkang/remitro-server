package com.remitro.event.member.event;

import java.time.LocalDateTime;

public record MemberDormantEvent(
	Long memberId,

	LocalDateTime occurredAt
) {
}
