package com.remitro.common.contract.member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAuthMapper {

	public static MemberCredentialsResponse toMemberCredentialsResponse(
		Long memberId,
		String email,
		String nickname,
		String password
	) {
		return new MemberCredentialsResponse(
			memberId,
			email,
			nickname,
			password
		);
	}
}
