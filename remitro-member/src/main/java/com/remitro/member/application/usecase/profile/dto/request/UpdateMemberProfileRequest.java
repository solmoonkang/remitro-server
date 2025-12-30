package com.remitro.member.application.usecase.profile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(name = "UpdateMemberProfileRequest", description = "회원 프로필 정보 수정 요청 DTO")
public record UpdateMemberProfileRequest(
	@Size(min = 2, max = 20, message = "닉네임은 2~20자 사이로 입력해주세요.")
	@Schema(description = "변경할 사용자 닉네임", example = "memberNickname")
	String nickname,

	@Pattern(regexp = "^01[0-9]{8,9}$", message = "전화번호 형식이 올바르지 않습니다.")
	@Schema(name = "변경할 사용자 전화번호 (하이픈 제외)", example = "01012345678")
	String phoneNumber
) {
}
