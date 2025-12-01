package com.remitro.member.application.mapper;

import com.remitro.member.domain.enums.ActivityStatus;
import com.remitro.member.domain.enums.KycStatus;
import com.remitro.member.domain.event.MemberCreatedEvent;
import com.remitro.member.domain.event.MemberKycUpdatedEvent;
import com.remitro.member.domain.event.MemberStatusUpdatedEvent;
import com.remitro.member.domain.model.Member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEventMapper {

	public static MemberCreatedEvent toMemberCreatedEvent(Member member) {
		return new MemberCreatedEvent(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			member.getActivityStatus().name(),
			member.getKycStatus().name(),
			member.isActiveForAccountOpen()
		);
	}

	public static MemberStatusUpdatedEvent toMemberStatusUpdatedEvent(Member member) {
		return new MemberStatusUpdatedEvent(
			member.getId(),
			member.getNickname(),
			member.getActivityStatus(),
			member.getKycStatus(),
			member.isActiveForAccountOpen()
		);
	}

	public static MemberKycUpdatedEvent toMemberKycUpdatedEvent(Member member) {
		return new MemberKycUpdatedEvent(
			member.getId(),
			member.getKycStatus(),
			member.getKycVerifiedAt(),
			member.isActiveForAccountOpen()
		);
	}
}
