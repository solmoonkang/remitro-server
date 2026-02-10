package com.remitro.account.application.command.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "계좌 송금 요청")
public record TransferRequest(
	@Schema(description = "송금 금액", example = "50000")
	@NotNull(message = "송금 금액을 입력해주세요.")
	@Min(value = 1, message = "송금 금액은 1원 이상이어야 합니다.")
	Long amount,

	@Schema(description = "계좌 비밀번호", example = "1234")
	@NotBlank(message = "계좌 비밀번호를 입력해주세요.")
	@Pattern(regexp = "\\d{4}", message = "비밀번호는 숫자 4자리여야 합니다.")
	String pinNumber,

	@Schema(description = "받는 분 통장 표시", example = "점심값")
	@Size(max = 20, message = "적요는 20자 이내여야 합니다.")
	String toDescription,

	@Schema(description = "내 통장 표시", example = "이철수(점심값)")
	@Size(max = 20, message = "적요는 20자 이내여야 합니다.")
	String fromDescription
) {
}
