package com.remitro.member.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(name = "회원가입 요청 DTO")
public record SignUpRequest(
	@NotBlank(message = "사용자 이메일을 입력해주세요.")
	@Schema(name = "사용자 이메일", example = "memberEmail")
	String email,

	@NotBlank(message = "사용자 비밀번호를 입력해주세요.")
	@Schema(name = "사용자 비밀번호", example = "memberPassword")
	String password,

	@NotBlank(message = "확인 비밀번호를 입력해주세요.")
	@Schema(name = "확인 비밀번호", example = "checkPassword")
	String checkPassword,

	@NotBlank(message = "사용자 닉네임을 입력해주세요.")
	@Schema(name = "사용자 닉네임", example = "memberNickname")
	String nickname
) {
}
