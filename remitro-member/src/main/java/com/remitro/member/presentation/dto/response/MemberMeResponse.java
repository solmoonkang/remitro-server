package com.remitro.member.presentation.dto.response;

import com.remitro.member.domain.member.model.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "MemberMeResponse", description = "내 정보 조회 응답 DTO")
public record MemberMeResponse(
	@Schema(name = "회원ID", example = "999L")
	Long memberId,

	@Schema(name = "회원 이메일", example = "memberEmail")
	String email,

	@Schema(name = "회원 닉네임", example = "memberNickname")
	String nickname,

	@Schema(name = "회원 전화번호", example = "memberPhoneNumber")
	String phoneNumber
) {

	public static MemberMeResponse from(Member member) {
		return new MemberMeResponse(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			member.getPhoneNumber()
		);
	}
}
