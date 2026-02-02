package com.remitro.account.application.command.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "입출금 계좌 개설 응답")
public record DepositOpenResponse(
	@Schema(
		description = "계좌 번호",
		example = "110-99-456789"
	)
	String formattedAccountNumber,

	@Schema(
		description = "계좌 별칭",
		example = "생활비 통장"
	)
	String accountAlias,

	@Schema(
		description = "계좌 생성 시각",
		example = "2026-01-28T15:00:00"
	)
	LocalDateTime createdAt
) {
}
