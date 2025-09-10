package com.remitro.account.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CreateAccountRequest(
	@NotBlank(message = "계좌 이름을 입력해주세요.")
	String accountName,

	@NotBlank(message = "계좌 비밀번호를 입력해주세요.")
	@Pattern(regexp = "^[0-9]{4}$", message = "계좌 비밀번호는 4자리 숫자만 입력 가능합니다.")
	String password,

	@NotBlank(message = "계좌 타입을 입력해주세요.")
	String accountType
) {
}
