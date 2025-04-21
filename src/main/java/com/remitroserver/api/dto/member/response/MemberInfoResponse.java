package com.remitroserver.api.dto.member.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record MemberInfoResponse(
	@Schema(description = "사용자 닉네임", example = "memberNickname")
	String nickname,

	@Schema(description = "마스킹된 사용자 주민등록번호", example = "111111-*******")
	String registrationNumber,

	@Schema(description = "가입 일자", example = "2024-01-02T15:30:00")
	LocalDateTime createdAt
) {
}
