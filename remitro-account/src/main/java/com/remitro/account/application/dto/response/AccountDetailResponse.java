package com.remitro.account.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "계좌 정보 응답 DTO")
public record AccountDetailResponse(
	@Schema(description = "계좌 소유자 닉네임", example = "memberNickname")
	String nickname,

	@Schema(description = "계좌 번호", example = "110-431-402158")
	String accountNumber,

	@Schema(description = "계좌 이름", example = "accountName")
	String accountName,

	@Schema(description = "계좌 잔액", example = "10,000L")
	Long balance,

	@Schema(description = "계좌 활성화 여부", example = "TRUE, FALSE")
	boolean isActivated,

	@Schema(description = "계좌 타입", example = "CHECKING, SAVINGS, DEPOSIT")
	String accountType,

	@Schema(description = "계좌 상태", example = "NORMAL, FROZEN, TERMINATED, DORMANT")
	String accountStatus
) {
}
