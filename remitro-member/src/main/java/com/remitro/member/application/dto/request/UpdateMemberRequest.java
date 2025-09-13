package com.remitro.member.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(name = "사용자 정보 수정 요청 DTO")
public record UpdateMemberRequest(
	@NotBlank(message = "사용자 닉네임을 입력해주세요.")
	@Schema(name = "사용자 닉네임", example = "memberNickname")
	String nickname
) {
}
