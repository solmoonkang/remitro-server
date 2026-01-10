package com.remitro.account.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(name = "LoanAccountCreateRequest", description = "대출 계좌 생성 요청 DTO")
public record LoanAccountCreateRequest(
	@NotNull(message = "대출 승인 여부를 입력해주세요.")
	@Schema(description = "대출 승인 여부", example = "true/false")
	boolean loanApproved
) {
}
