package com.remitroserver.api.dto.account.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.remitroserver.api.domain.account.model.AccountStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "계좌 목록 조회 응답 DTO")
public record AccountSummaryResponse(
	@Schema(description = "계좌 번호", example = "110-01-123456")
	String accountNumber,

	@Schema(description = "계좌 고유 식별 토큰 (상세 조회 요청 시 사용)", example = "3e5d6c92-1fd8-453c-920d-3c77eb13d345")
	UUID accountToken,

	@Schema(description = "계좌 잔액", example = "10,000")
	Long balance,

	@Schema(description = "계좌 상태", example = "ACTIVE")
	AccountStatus status,

	@Schema(description = "계좌 생성일", example = "2024-04-21T15:30:00")
	LocalDateTime createdAt
) {
}
