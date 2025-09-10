package com.remitro.account.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TransferFormRequest(
	@NotBlank(message = "받는 사용자의 계좌번호를 입력해주세요.")
	String receiverAccountNumber,

	@Min(value = 1, message = "송금액은 1원 이상이어야 합니다.")
	Long amount,

	@NotBlank(message = "계좌 비밀번호를 입력해주세요.")
	String password
) {
}
