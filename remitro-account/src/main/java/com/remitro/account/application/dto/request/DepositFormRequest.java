package com.remitro.account.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
@Schema(description = "계좌 입금 요청 DTO")
public record DepositFormRequest(
	@Min(value = 1, message = "입금액은 1원 이상이어야 합니다.")
	@Schema(description = "입금 금액", example = "1,000L")
	Long amount
) {
}
