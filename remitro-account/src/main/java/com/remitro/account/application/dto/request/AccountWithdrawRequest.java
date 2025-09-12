package com.remitro.account.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "계좌 출금 요청 DTO")
public record AccountWithdrawRequest(
	@Min(value = 1, message = "출금액은 1원 이상이어야 합니다.")
	@Schema(description = "출금 금액", example = "1,000L")
	Long amount,

	@NotBlank(message = "계좌 비밀번호를 입력해주세요.")
	@Schema(description = "계좌 비밀번호", example = "accountPassword")
	String password
) {
}
