package com.remitro.account.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AccountWithdrawRequest(
	@Min(value = 1, message = "출금액은 1원 이상이어야 합니다.")
	Long amount,

	@NotBlank(message = "계좌 비밀번호를 입력해주세요.")
	String password
) {
}
