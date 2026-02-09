package com.remitro.event.domain.member.model;

import java.time.LocalDateTime;

public record MemberProfileUpdatedEvent(
	Long memberId,

	String nickname,

	LocalDateTime updatedAt
) {
}
