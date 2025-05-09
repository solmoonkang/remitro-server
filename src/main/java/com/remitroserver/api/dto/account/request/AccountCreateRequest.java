package com.remitroserver.api.dto.account.request;

import com.remitroserver.api.domain.account.model.AccountType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
@Schema(description = "계좌 생성 요청 DTO")
public record AccountCreateRequest(
	@Schema(description = "계좌 타입", example = "CHECKING, SAVING, CMA")
	@NotNull(message = "계좌 타입은 필수 입력 항목입니다.")
	AccountType accountType,

	@Schema(description = "사용자 계좌 비밀번호", example = "encodedAccountPassword")
	@NotBlank(message = "계좌 비밀번호를 입력해주세요.")
	@Pattern(regexp = "^\\d{4}$", message = "비밀번호는 4자리 숫자여야 합니다.")
	String accountPassword,

	@Schema(description = "사용자 계좌 확인용 비밀번호", example = "encodedAccountPassword")
	@NotBlank(message = "확인 비밀번호를 입력해주세요.")
	@Pattern(regexp = "^\\d{4}$", message = "비밀번호는 4자리 숫자여야 합니다.")
	String accountCheckPassword
) {
}
