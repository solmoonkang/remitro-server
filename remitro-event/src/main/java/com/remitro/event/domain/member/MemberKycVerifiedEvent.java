package com.remitro.event.domain.member;

import java.time.LocalDateTime;

public record MemberKycVerifiedEvent(
	Long memberId,

	Long adminMemberId,

	LocalDateTime occurredAt
) {
}
