package com.remitro.member.application.common.mapper;

import com.remitro.member.application.usecase.query.dto.response.MemberInfoResponse;
import com.remitro.member.domain.member.model.Member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberMapper {

	public static MemberInfoResponse toMemberInfoResponse(Member member) {
		return new MemberInfoResponse(
			member.getEmail(),
			member.getNickname(),
			member.getPhoneNumber()
		);
	}
}
