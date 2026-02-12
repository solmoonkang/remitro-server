package com.remitro.account.application.command.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "계좌 출금 응답")
public record WithdrawResponse(
	@Schema(description = "거래 번호 (원장 ID)", example = "999L")
	Long ledgerId,

	@Schema(description = "계좌 번호", example = "110-99-456789")
	String formattedAccountNumber,

	@Schema(description = "출금 금액 (포맷팅)", example = "30,000")
	String formattedWithdrawAmount,

	@Schema(description = "거래 후 잔액 (포맷팅)", example = "120,000")
	String formattedCurrentBalance,

	@Schema(description = "거래 일시", example = "2026-02-09T20:45:00")
	LocalDateTime transactedAt
) {
}
