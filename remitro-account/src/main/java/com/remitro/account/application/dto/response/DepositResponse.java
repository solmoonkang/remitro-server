package com.remitro.account.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "입금 응답 DTO")
public record DepositResponse(
	@Schema(description = "계좌 고유 번호", example = "1")
	Long accountId,

	@Schema(description = "입금 금액", example = "10000")
	Long amount,

	@Schema(description = "입금 후 잔액", example = "30000")
	Long balanceAfter
) {
}
