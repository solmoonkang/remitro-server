package com.remitro.account.application.usecase.query.dto.response;

import com.remitro.account.domain.account.enums.LifecycleStatus;
import com.remitro.account.domain.product.enums.ProductType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "AccountSummaryResponse", description = "계좌 요약 응답 DTO")
public record AccountSummaryResponse(
	@Schema(description = "계좌 ID", example = "999L")
	Long accountId,

	@Schema(description = "계좌 번호", example = "110-123-456789")
	String accountNumber,

	@Schema(description = "계좌 이름", example = "생활비 통장")
	String accountName,

	@Schema(description = "계좌 상품 유형", example = "CHECKING")
	ProductType productType,

	@Schema(description = "계좌 상태", example = "NORMAL")
	LifecycleStatus lifecycleStatus,

	@Schema(description = "잔액", example = "1500000")
	Long balance
) {
}
