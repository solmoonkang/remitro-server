package com.remitro.account.application.command.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "계좌 입금 응답")
public record DepositResponse(
	@Schema(description = "거래 번호 (원장 ID)", example = "999L")
	Long ledgerId,

	@Schema(description = "계좌 번호", example = "110-99-456789")
	String formattedAccountNumber,

	@Schema(description = "입금 금액", example = "50,000")
	String formattedDepositAmount,

	@Schema(description = "거래 후 잔액", example = "150,000")
	String formattedCurrentBalance,

	@Schema(description = "거래 일시", example = "2026-02-09T19:00:00")
	LocalDateTime transactedAt
) {
}
