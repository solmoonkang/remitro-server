package com.remitro.account.application.dto.response;

import java.time.LocalDateTime;

import com.remitro.account.domain.enums.AccountStatus;
import com.remitro.account.domain.enums.AccountType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "계좌 정보 응답 DTO")
public record AccountDetailResponse(
	@Schema(description = "계좌 고유 번호", example = "1")
	Long accountId,

	@Schema(description = "계좌 번호", example = "110-431-402158")
	String accountNumber,

	@Schema(description = "계좌 이름", example = "accountName")
	String accountName,

	@Schema(description = "계좌 잔액", example = "25000")
	Long balance,

	@Schema(description = "계좌 타입", example = "CHECKING", implementation = AccountType.class)
	AccountType accountType,

	@Schema(description = "계좌 상태", example = "NORMAL", implementation = AccountStatus.class)
	AccountStatus accountStatus,

	@Schema(description = "계좌 생성 일시", example = "2025-03-01T10:15:30")
	LocalDateTime createdAt
) {
}
