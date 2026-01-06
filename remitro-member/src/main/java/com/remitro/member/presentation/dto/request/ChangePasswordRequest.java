package com.remitro.member.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(name = "ChangePasswordRequest", description = "비밀번호 변경 요청 DTO")
public record ChangePasswordRequest(
	@NotBlank(message = "현재 비밀번호를 입력해 주세요.")
	@Schema(name = "현재 비밀번호", example = "currentPassword")
	String currentPassword,

	@NotBlank(message = "새 비밀번호를 입력해 주세요.")
	@Size(min = 8, max = 20, message = "비밀번호는 8~20자 사이로 입력해주세요.")
	@Schema(name = "새 비밀번호", example = "newPassword")
	String newPassword,

	@NotBlank(message = "비밀번호 확인을 입력해 주세요.")
	@Schema(name = "확인 비밀번호", example = "confirmPassword")
	String confirmPassword
) {
}
