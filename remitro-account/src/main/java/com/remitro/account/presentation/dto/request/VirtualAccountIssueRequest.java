package com.remitro.account.presentation.dto.request;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(name = "VirtualAccountIssueRequest", description = "가상 계좌 발급 요청 DTO")
public record VirtualAccountIssueRequest(
	@NotNull(message = "가상 계좌 만료 시각을 입력해주세요.")
	@Schema(description = "가상 계좌 만료 시각", example = "2026-01-31T23:59:59")
	LocalDateTime expiredAt
) {
}
