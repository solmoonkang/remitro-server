package com.remitro.event.member.event;

import java.time.LocalDateTime;

public record MemberProfileUpdatedEvent(
	Long memberId,

	String nickname,

	String phoneNumber,

	LocalDateTime occurredAt
) {
}
