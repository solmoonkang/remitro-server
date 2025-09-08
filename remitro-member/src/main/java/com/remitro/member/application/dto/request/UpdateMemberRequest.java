package com.remitro.member.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateMemberRequest(
	@NotBlank(message = "사용자 닉네임을 입력해주세요.")
	String nickname
) {
}
