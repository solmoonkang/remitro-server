package com.remitro.event.domain.member;

import java.time.LocalDateTime;

public record MemberDormantEvent(
	Long memberId,

	LocalDateTime occurredAt
) {
}
