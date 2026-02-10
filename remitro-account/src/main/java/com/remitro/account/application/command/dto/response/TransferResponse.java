package com.remitro.account.application.command.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "계좌 송금 응답")
public record TransferResponse(
	@Schema(description = "출금 거래 번호 (원장 ID)", example = "999L")
	Long fromLedgerId,

	@Schema(description = "입금 거래 번호 (원장 ID)", example = "998L")
	Long toLedgerId,

	@Schema(description = "보낸 계좌 번호", example = "110-12-345678")
	String fromFormattedAccountNumber,

	@Schema(description = "받는 계좌 번호", example = "202-33-987654")
	String toFormattedAccountNumber,

	@Schema(description = "송금 금액 (포맷팅)", example = "50,000")
	String formattedAmount,

	@Schema(description = "송금 후 잔액 (포맷팅)", example = "150,000")
	String formattedBalance,

	@Schema(description = "거래 일시", example = "2026-02-10T17:30:00")
	LocalDateTime transactedAt
) {
}
