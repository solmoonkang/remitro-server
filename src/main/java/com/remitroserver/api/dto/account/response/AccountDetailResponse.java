package com.remitroserver.api.dto.account.response;

import java.time.LocalDateTime;

import com.remitroserver.api.domain.account.model.AccountType;
import com.remitroserver.api.domain.account.model.Status;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "계좌 상세 조회 응답 DTO")
public record AccountDetailResponse(
	@Schema(description = "계좌 번호", example = "110-01-123456")
	String accountNumber,

	@Schema(description = "계좌 유형", example = "CHECKING")
	AccountType accountType,

	@Schema(description = "계좌 잔액", example = "100,000")
	Long balance,

	@Schema(description = "계좌 상태", example = "ACTIVE")
	Status status,

	@Schema(description = "계좌 생성일", example = "2024-04-21T15:30:00")
	LocalDateTime createdAt,

	@Schema(description = "계좌 소유자 닉네임", example = "memberNickname")
	String ownerNickname
) {
}
