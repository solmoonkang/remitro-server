package com.remitro.account.application.usecase.query.dto.response;

import java.time.LocalDateTime;

import com.remitro.account.domain.account.enums.LifecycleStatus;
import com.remitro.account.domain.product.enums.ProductType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "AccountDetailResponse", description = "계좌 상세 조회 응답 DTO")
public record AccountDetailResponse(
	@Schema(description = "계좌 ID", example = "999L")
	Long accountId,

	@Schema(description = "계좌 번호", example = "110-123-456789")
	String accountNumber,

	@Schema(description = "계좌 이름", example = "생활비 계좌")
	String accountName,

	@Schema(description = "계좌 유형", example = "DEPOSIT")
	ProductType productType,

	@Schema(description = "계좌 상태", example = "NORMAL")
	LifecycleStatus lifecycleStatus,

	@Schema(description = "잔액", example = "100000")
	Long balance,

	@Schema(description = "계좌 생성 일시", example = "2025-01-01T12:00:00")
	LocalDateTime createdAt
) {
}
