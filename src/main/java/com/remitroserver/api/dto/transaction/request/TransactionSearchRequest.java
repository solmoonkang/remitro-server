package com.remitroserver.api.dto.transaction.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.remitroserver.api.domain.transaction.model.TransactionStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "거래 내역 검색 조건 DTO")
public record TransactionSearchRequest(
	@Schema(description = "조회 시작일 (yyyy-MM-dd)", example = "2024-04-01")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	LocalDate fromDate,

	@Schema(description = "조회 종료일 (yyyy-MM-dd)", example = "2024-04-30")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	LocalDate toDate,

	@Schema(description = "거래 상태", example = "COMPLETED")
	TransactionStatus status
) {
}
