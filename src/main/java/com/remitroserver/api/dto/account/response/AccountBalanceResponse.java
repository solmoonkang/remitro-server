package com.remitroserver.api.dto.account.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "계좌 잔액 조회 응답 DTO")
public record AccountBalanceResponse(
	@Schema(description = "계좌 잔액", example = "100,000")
	Long balance,

	@Schema(description = "잔액 조회 일시", example = "2024-04-22T13:40:00")
	LocalDateTime retrievedAt
) {
}
