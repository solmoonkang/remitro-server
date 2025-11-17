package com.remitro.account.application.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 보유 계좌 목록 응답 DTO")
public record AccountsSummaryResponse(
	@Schema(description = "회원 고유 번호", example = "1")
	Long memberId,

	@Schema(description = "회원 닉네임", example = "memberNickname")
	String nickname,

	@Schema(description = "보유 계좌 목록")
	List<AccountSummaryResponse> accountSummaryResponses
) {
}
