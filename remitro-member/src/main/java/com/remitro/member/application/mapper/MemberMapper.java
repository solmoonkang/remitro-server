package com.remitro.member.application.mapper;

import com.remitro.member.application.read.account.dto.MemberProfileResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberMapper {

	public static MemberProfileResponse toMemberProfileResponse(
		String maskEmail,
		String nickname,
		String maskPhoneNumber
	) {
		return new MemberProfileResponse(
			maskEmail,
			nickname,
			maskPhoneNumber
		);
	}
}
