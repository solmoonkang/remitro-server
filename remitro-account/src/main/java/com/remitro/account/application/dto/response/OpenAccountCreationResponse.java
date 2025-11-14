package com.remitro.account.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "계좌 개설 응답 DTO")
public record OpenAccountCreationResponse(
	@Schema(description = "계좌 ID", example = "999L")
	Long accountId,

	@Schema(description = "계좌 번호", example = "110-431-402158")
	String accountNumber
) {
}
