package com.remitro.transaction.application.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "거래내역 정보 응답 DTO")
public record TransactionDetailResponse(
	@Schema(description = "송금 계좌 번호", example = "110-431-402158")
	String senderAccountNumber,

	@Schema(description = "입금 계좌 번호", example = "110-432-607198")
	String receiverAccountNumber,

	@Schema(description = "거래 유형", example = "DEPOSIT, WITHDRAWAL, TRANSFER")
	String transactionType,

	@Schema(description = "거래 금액", example = "10,000L")
	Long amount,

	@Schema(description = "거래 후 계좌 잔액", example = "25,000L")
	Long balanceSnapshot,

	@Schema(description = "거래 발생 일시", example = "2025-09-15T10:30:00")
	LocalDateTime transactionAt
) {
}
