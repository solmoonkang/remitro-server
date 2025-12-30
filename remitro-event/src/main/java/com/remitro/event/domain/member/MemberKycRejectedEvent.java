package com.remitro.event.domain.member;

import java.time.LocalDateTime;

public record MemberKycRejectedEvent(
	Long memberId,

	Long adminMemberId,

	String reason,

	LocalDateTime occurredAt
) {
}
