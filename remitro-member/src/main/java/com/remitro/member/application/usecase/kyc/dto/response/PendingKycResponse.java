package com.remitro.member.application.usecase.kyc.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "PendingKycResponse", description = "KYC 심사 대기 회원 응답 DTO")
public record PendingKycResponse(
	@Schema(name = "사용자 ID", example = "999L")
	Long memberId,

	@Schema(name = "사용자 이메일", example = "member@example.com")
	String email,

	@Schema(name = "사용자 닉네임", example = "memberNickname")
	String nickname,

	@Schema(name = "KYC 요청 일시", example = "2025-01-10T13:45:00")
	LocalDateTime requestedAt
) {
}
