package com.remitroserver.api.dto.transaction.response;

import java.time.LocalDateTime;

import com.remitroserver.api.domain.transaction.model.TransactionStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "거래 상태 로그 DTO")
public record TransactionStatusLogResponse(
	@Schema(description = "상태 값", example = "CANCELLED")
	TransactionStatus status,

	@Schema(description = "변경 시각", example = "2024-04-25T14:15:00")
	LocalDateTime changedAt
) {
}
