package com.remitro.member.application.usecase.signup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(name = "SignUpRequest", description = "회원가입 요청 DTO")
public record SignUpRequest(
	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "이메일 형식이 올바르지 않습니다.")
	@Schema(name = "사용자 이메일", example = "member@example.com")
	String email,

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Size(min = 8, max = 20, message = "비밀번호는 8~20자 사이로 입력해주세요.")
	@Schema(name = "사용자 비밀번호", example = "memberPassword")
	String password,

	@NotBlank(message = "확인 비밀번호를 입력해주세요.")
	@Schema(name = "확인 비밀번호", example = "checkPassword")
	String checkPassword,

	@NotBlank(message = "닉네임을 입력해주세요.")
	@Size(min = 2, max = 20, message = "닉네임은 2~20자 사이로 입력해주세요.")
	@Schema(name = "사용자 닉네임", example = "memberNickname")
	String nickname,

	@NotBlank(message = "전화번호를 입력해주세요.")
	@Pattern(regexp = "^01[0-9]{8,9}$", message = "전화번호 형식이 올바르지 않습니다.")
	@Schema(name = "사용자 전화번호 (하이픈 제외)", example = "01012345678")
	String phoneNumber
) {
}
