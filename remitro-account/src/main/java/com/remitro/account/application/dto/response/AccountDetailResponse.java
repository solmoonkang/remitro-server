package com.remitro.account.application.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record AccountDetailResponse(
	String nickname,

	String accountNumber,

	String accountName,

	Long balance,

	List<TransactionDetailResponse> transactionHistories,

	String accountStatus
) {
}
