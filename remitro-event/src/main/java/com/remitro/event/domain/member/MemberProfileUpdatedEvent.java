package com.remitro.event.domain.member;

import java.time.LocalDateTime;

public record MemberProfileUpdatedEvent(
	Long memberId,

	String nickname,

	String phoneNumber,

	LocalDateTime occurredAt
) {
}
