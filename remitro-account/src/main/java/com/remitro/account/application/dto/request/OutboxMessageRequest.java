package com.remitro.account.application.dto.request;

import com.remitro.common.domain.enums.AggregateType;
import com.remitro.common.domain.enums.EventType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "이벤트 발행 요청 DTO")
public record OutboxMessageRequest(
	@Schema(description = "이벤트 고유 ID", example = "0f5a7d8c-c2b3-4e4f-b67f-8d9e2a1b3c4d")
	String eventId,

	@Schema(description = "이벤트 발생 도메인 ID", example = "1001L")
	Long aggregateId,

	@Schema(description = "이벤트 발생 도메인 타입", example = "ACCOUNT, TRANSACTION, MEMBER")
	AggregateType aggregateType,

	@Schema(description = "이벤트 구체 타입", example = "DEPOSIT_EVENT, WITHDRAWAL_EVENT, TRANSFER_EVENT")
	EventType eventType,

	@Schema(description = "이벤트 실제 데이터", example = "{\"amount\":10000, \"accountNumber\":\"123-456\"}")
	String eventData
) {
}
