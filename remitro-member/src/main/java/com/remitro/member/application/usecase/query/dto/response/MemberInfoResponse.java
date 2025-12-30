package com.remitro.member.application.usecase.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "MemberInfoResponse", description = "사용자 정보 응답 DTO")
public record MemberInfoResponse(
	@Schema(name = "사용자 이메일", example = "member@example.com")
	String email,

	@Schema(name = "사용자 닉네임", example = "memberNickname")
	String nickname,

	@Schema(name = "사용자 전화번호", example = "memberPhoneNumber")
	String phoneNumber
) {
}
