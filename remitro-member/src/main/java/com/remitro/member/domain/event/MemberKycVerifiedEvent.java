package com.remitro.member.domain.event;

import java.time.LocalDateTime;

public record MemberKycVerifiedEvent(
	Long memberId,

	Long verifiedBy,

	boolean isMemberActive,

	LocalDateTime verifiedAt
) {
}
