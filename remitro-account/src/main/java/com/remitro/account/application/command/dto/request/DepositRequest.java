package com.remitro.account.application.command.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "계좌 입금 요청")
public record DepositRequest(
	@Schema(description = "입금 금액", example = "50000")
	@NotNull(message = "입금 금액을 입력해주세요.")
	@Min(value = 1, message = "입금 금액은 최소 1원 이상이어야 합니다.")
	Long amount,

	@Schema(description = "거래 적요", example = "현금 입금")
	@Size(max = 100, message = "적요는 최대 100자 이내로 입력해주세요.")
	String description
) {
}
