package com.remitro.account.application.dto.request;

import com.remitro.account.domain.enums.AccountType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
@Schema(description = "계좌 개설 요청 DTO")
public record OpenAccountRequest(
	@NotBlank(message = "계좌 이름을 입력해주세요.")
	@Schema(description = "계좌 이름", example = "accountName")
	String accountName,

	@NotBlank(message = "계좌 비밀번호를 입력해주세요.")
	@Pattern(regexp = "^[0-9]{4}$", message = "계좌 비밀번호는 4자리 숫자만 입력 가능합니다.")
	@Schema(description = "계좌 비밀번호", example = "accountPassword")
	String password,

	@NotBlank(message = "계좌 타입을 입력해주세요.")
	@Schema(description = "계좌 타입", example = "CHECKING, SAVINGS, DEPOSIT")
	AccountType accountType
) {
}
