package com.remitro.member.application.mapper;

import java.time.LocalDateTime;

import com.remitro.common.security.Role;
import com.remitro.member.domain.event.MemberCreatedEvent;
import com.remitro.member.domain.event.MemberKycRejectedEvent;
import com.remitro.member.domain.event.MemberKycRequestedEvent;
import com.remitro.member.domain.event.MemberKycVerifiedEvent;
import com.remitro.member.domain.event.MemberRoleUpdatedEvent;
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

	public static MemberKycRequestedEvent toMemberKycRequestedEvent(Member member) {
		return new MemberKycRequestedEvent(
			member.getId(),
			LocalDateTime.now()
		);
	}

	public static MemberKycVerifiedEvent toMemberKycVerifiedEvent(Member member, Long adminMemberId) {
		return new MemberKycVerifiedEvent(
			member.getId(),
			adminMemberId,
			member.isActiveForAccountOpen(),
			member.getKycVerifiedAt()
		);
	}

	public static MemberKycRejectedEvent toMemberKycRejectedEvent(Member member, Long adminMemberId, String reason) {
		return new MemberKycRejectedEvent(
			member.getId(),
			adminMemberId,
			reason,
			LocalDateTime.now()
		);
	}

	public static MemberRoleUpdatedEvent toMemberRoleUpdatedEvent(
		Member member,
		Role previousRole,
		Long adminMemberId
	) {
		return new MemberRoleUpdatedEvent(
			member.getId(),
			previousRole,
			member.getRole(),
			adminMemberId,
			LocalDateTime.now()
		);
	}
}
