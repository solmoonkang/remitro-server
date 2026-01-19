package com.remitro.member.application.command.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "비밀번호 변경 요청")
public record PasswordChangeRequest(
	@Schema(
		description = "현재 비밀번호",
		example = "oldP@ss0rd!"
	)
	@Size(min = 8, max = 20, message = "비밀번호는 최소 8자 이상 20자 이하로 입력해주세요.")
	@NotBlank(message = "현재 비밀번호를 입력해주세요.")
	String currentPassword,

	@Schema(
		description = "변경할 새 비밀번호",
		example = "newP@ss0rd!"
	)
	@Size(min = 8, max = 20, message = "비밀번호는 최소 8자 이상 20자 이하로 입력해주세요.")
	@NotBlank(message = "새 비밀번호를 입력해주세요.")
	String newPassword,

	@Schema(
		description = "새 비밀번호 확인 (새 비밀번호와 동일)",
		example = "newP@ss0rd!"
	)
	@Size(min = 8, max = 20, message = "비밀번호는 최소 8자 이상 20자 이하로 입력해주세요.")
	@NotBlank(message = "확인 비밀번호를 입력해주세요. 새 비밀번호와 동일하게 입력해주세요.")
	String confirmPassword
) {
}
