package com.remitroserver.api.application.member.mapper;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.dto.member.response.MemberInfoResponse;

public class MemberMapper {

	private MemberMapper() {
		throw new UnsupportedOperationException(UTILITY_CLASS_INSTANTIATION_ERROR.getMessage());
	}

	public static MemberInfoResponse toInfoResponse(Member member, String registrationNumber) {
		return MemberInfoResponse.builder()
			.nickname(member.getNickname())
			.registrationNumber(registrationNumber)
			.createdAt(member.getCreatedAt())
			.build();
	}
}
