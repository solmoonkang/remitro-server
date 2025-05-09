package com.remitroserver.api.dto.account.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
@Schema(description = "계좌 입금/출금 요청 DTO")
public record AccountAmountRequest(
	@Schema(description = "입금 또는 출금할 금액", example = "10,000", minimum = "1", type = "integer")
	@NotNull(message = "입금 또는 출금 금액은 필수 항목입니다.")
	@Positive(message = "입금 또는 출금 금액은 1원 이상이어야 합니다.")
	Long amount,

	@Schema(description = "사용자 계좌 비밀번호", example = "encodedAccountPassword")
	@NotBlank(message = "계좌 비밀번호를 입력해주세요.")
	String accountPassword
) {
}
