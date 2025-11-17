package com.remitro.account.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "계좌 잔액 조회 응답 DTO")
public record AccountBalanceResponse(
	@Schema(description = "계좌 잔액", example = "25000")
	Long balance
) {
}
