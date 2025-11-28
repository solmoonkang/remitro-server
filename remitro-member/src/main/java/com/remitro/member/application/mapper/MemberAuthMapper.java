package com.remitro.member.application.mapper;

import com.remitro.common.contract.MemberAuthInfo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAuthMapper {

	public static MemberAuthInfo toMemberCredentialsResponse(
		Long memberId,
		String email,
		String nickname,
		String password
	) {
		return new MemberAuthInfo(
			memberId,
			email,
			nickname,
			password
		);
	}
}
