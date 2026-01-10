package com.remitro.account.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "AccountOpenResponse", description = "계좌 개설 응답 DTO")
public record AccountOpenResponse(
	@Schema(description = "계좌 번호", example = "110-456-405414")
	String accountNumber
) {
}
