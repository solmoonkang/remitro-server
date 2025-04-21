package com.remitroserver.api.dto.account.request;

import com.remitroserver.api.domain.account.model.AccountType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AccountCreateRequest(
	@Schema(description = "계좌 타입", example = "CHECKING, SAVING, CMA")
	@NotNull(message = "계좌 타입은 필수 입력 항목입니다.")
	AccountType accountType
) {
}
