package com.remitro.account.application.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
public record AccountDepositRequest(
	@Min(value = 1, message = "입금액은 1원 이상이어야 합니다.")
	Long amount
) {
}
