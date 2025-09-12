package com.remitro.account.application.dto.response;

import lombok.Builder;

@Builder
public record AccountDetailResponse(
	String nickname,

	String accountNumber,

	String accountName,

	Long balance,

	boolean isActivated,

	String accountType,

	String accountStatus
) {
}
