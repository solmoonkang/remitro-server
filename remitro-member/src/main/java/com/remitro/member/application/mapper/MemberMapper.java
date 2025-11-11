package com.remitro.member.application.mapper;

import com.remitro.common.contract.member.MemberStatusChangedEvent;
import com.remitro.member.application.dto.response.MemberInfoResponse;
import com.remitro.member.domain.model.Member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberMapper {

	public static MemberInfoResponse toMemberInfoResponse(Member member) {
		return new MemberInfoResponse(member.getEmail(), member.getNickname(), member.getPhoneNumber());
	}

	public static MemberStatusChangedEvent toMemberStatusChangedEvent(Member member) {
		return new MemberStatusChangedEvent(member.getId(), member.getActivityStatus());
	}
}
