package com.remitro.member.application.mapper;

import com.remitro.common.auth.MemberAuthInfo;
import com.remitro.member.domain.model.Member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAuthMapper {

	public static MemberAuthInfo toMemberAuthInfo(Member member) {
		return new MemberAuthInfo(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			member.getHashedPassword(),
			member.getRole()
		);
	}
}
