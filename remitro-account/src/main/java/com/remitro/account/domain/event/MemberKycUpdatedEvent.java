package com.remitro.account.domain.event;

import java.time.LocalDateTime;

public record MemberKycUpdatedEvent(
	Long memberId,

	String kycStatus,

	LocalDateTime verifiedAt,

	boolean isAccountOpenAllowed
) {
}
