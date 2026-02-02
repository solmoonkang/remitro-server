package com.remitro.account.application.command.dto.request;

import com.remitro.account.domain.account.enums.AccountType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "입출금 계좌 개설 요청")
public record DepositOpenRequest(
	@Schema(
		description = "계좌 별칭",
		example = "생활비 통장"
	)
	@Size(max = 30, message = "계좌 별칭은 최대 30자 이내로 입력해주세요.")
	String accountAlias,

	@Schema(
		description = "계좌 타입",
		example = "DEPOSIT"
	)
	@NotNull(message = "계좌 타입을 입력해주세요.")
	AccountType accountType,

	@Schema(
		description = "계좌 비밀번호",
		example = "1234"
	)
	@NotBlank(message = "계좌 비밀번호를 입력해주세요.")
	@Size(
		min = 4, max = 4,
		message = "계좌 비밀번호 4자리를 입력해주세요."
	)
	@Pattern(
		regexp = "\\d{4}",
		message = "계좌 비밀번호를 4자리 숫자로 입력해주세요."
	)
	String pinNumber,

	@Schema(
		description = "계좌 비밀번호 확인 (계좌 비밀번호와 동일)",
		example = "1234"
	)
	@NotBlank(message = "계좌 비밀번호를 입력해주세요.")
	@Size(
		min = 4, max = 4,
		message = "계좌 비밀번호 4자리를 입력해주세요."
	)
	@Pattern(
		regexp = "\\d{4}",
		message = "계좌 비밀번호를 4자리 숫자로 입력해주세요."
	)
	String confirmPinNumber
) {
}
