package com.remitro.member.domain.event;

import java.time.LocalDateTime;

public record MemberKycRejectedEvent(
	Long memberId,

	Long rejectedBy,

	String reason,

	LocalDateTime rejectedAt
) {
}
