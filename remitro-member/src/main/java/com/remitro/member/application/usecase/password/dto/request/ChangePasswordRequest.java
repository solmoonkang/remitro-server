package com.remitro.member.application.usecase.password.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(name = "ChangePasswordRequest", description = "회원 비밀번호 변경 요청 DTO")
public record ChangePasswordRequest(
	@NotBlank(message = "현재 사용 중인 비밀번호를 입력해주세요.")
	@Schema(description = "현재 사용 중인 비밀번호", example = "memberCurrentPassword")
	String currentPassword,

	@NotBlank(message = "변경할 새로운 비밀번호를 입력해주세요.")
	@Size(min = 8, max = 20, message = "비밀번호는 8~20자 사이로 입력해주세요.")
	@Schema(description = "변경할 새로운 비밀번호", example = "memberNewPassword")
	String newPassword,

	@NotBlank(message = "변경할 새로운 비밀번호를 한 번 더 입력해주세요.")
	@Schema(description = "변경할 새로운 비밀번호 확인", example = "memberConfirmPassword")
	String confirmPassword
) {
}
