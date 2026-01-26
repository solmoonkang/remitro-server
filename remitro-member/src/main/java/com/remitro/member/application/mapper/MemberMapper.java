package com.remitro.member.application.mapper;

import java.time.LocalDateTime;

import com.remitro.member.application.read.account.dto.response.EmailFindResponse;
import com.remitro.member.application.read.account.dto.response.MemberProfileResponse;

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

	public static EmailFindResponse toEmailFindResponse(String maskEmail, LocalDateTime createdAt) {
		return new EmailFindResponse(maskEmail, createdAt);
	}
}
