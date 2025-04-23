package com.remitroserver.api.dto.transaction.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.remitroserver.api.domain.transaction.model.TransactionStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "거래 내역 요약 응답 DTO")
public record TransactionSummaryResponse(
	@Schema(description = "거래 고유 식별자 (UUID)", example = "e8b9d16a-9f54-4a6f-8e8f-123456789abc")
	UUID transactionToken,

	@Schema(description = "출금 계좌 번호", example = "110-01-123456")
	String fromAccountNumber,

	@Schema(description = "입금 계좌 번호", example = "110-01-654321")
	String toAccountNumber,

	@Schema(description = "거래 금액", example = "50000")
	Long amount,

	@Schema(description = "거래 상태", example = "COMPLETED")
	TransactionStatus status,

	@Schema(description = "거래 일시", example = "2024-04-24T13:45:00")
	LocalDateTime createdAt
) {
}
