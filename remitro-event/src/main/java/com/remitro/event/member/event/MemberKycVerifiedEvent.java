package com.remitro.event.member.event;

import java.time.LocalDateTime;

public record MemberKycVerifiedEvent(
	Long memberId,

	Long adminMemberId,

	LocalDateTime verifiedAt
) {
}
