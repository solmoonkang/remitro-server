package com.remitro.account.application.dto.request.deposit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "입금 요청 DTO")
public record DepositRequest(
	@NotNull(message = "입금 금액을 입력해주세요.")
	@Positive(message = "0원보다 큰 금액을 입력해주세요.")
	@Schema(description = "입금 금액", example = "10000")
	Long amount,

	@Schema(description = "입금 메모", example = "testDescription")
	String description
) {
}
