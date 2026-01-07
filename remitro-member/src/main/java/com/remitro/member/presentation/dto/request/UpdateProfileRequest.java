package com.remitro.member.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(name = "UpdateProfileRequest", description = "프로필 정보 수정 요청 DTO")
public record UpdateProfileRequest(
	@NotBlank(message = "닉네임을 입력해주세요.")
	@Size(min = 2, max = 20, message = "닉네임은 2~20자 사이로 입력해주세요.")
	@Schema(name = "변경할 닉네임", example = "newNickname")
	String nickname,

	@NotBlank(message = "전화번호를 입력해주세요.")
	@Pattern(regexp = "^01[0-9]{8,9}$", message = "전화번호 형식이 올바르지 않습니다.")
	@Schema(name = "변경할 전화번호 (하이픈 제외)", example = "01012345678")
	String phoneNumber
) {
}
