package com.remitro.event.member.event;

import java.time.LocalDateTime;

public record MemberKycRejectedEvent(
	Long memberId,

	Long adminMemberId,

	String reason,

	LocalDateTime occurredAt
) {
}
