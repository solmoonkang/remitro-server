package com.remitro.account.application.command.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "계좌 출금 요청")
public record WithdrawRequest(
	@Schema(description = "출금 금액", example = "30000")
	@NotNull(message = "출금 금액을 입력해주세요.")
	@Min(value = 1, message = "출금 금액은 최소 1원 이상이어야 합니다.")
	Long amount,

	@Schema(description = "계좌 비밀번호 (4자리 숫자)", example = "1234")
	@NotBlank(message = "계좌 비밀번호를 입력해주세요.")
	@Pattern(regexp = "\\d{4}", message = "계좌 비밀번호는 4자리 숫자로 입력해주세요.")
	String pinNumber,

	@Schema(description = "거래 적요 (나에게 쓰는 메모)", example = "점심값 출금")
	@Size(max = 100, message = "적요는 최대 100자 이내로 입력해주세요.")
	String description
) {
}
