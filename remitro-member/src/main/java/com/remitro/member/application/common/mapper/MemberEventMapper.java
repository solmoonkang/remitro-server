package com.remitro.member.application.common.mapper;

import java.time.LocalDateTime;

import com.remitro.common.security.Role;
import com.remitro.event.domain.member.enums.LockActorType;
import com.remitro.event.domain.member.enums.MemberActivityStatus;
import com.remitro.event.domain.member.enums.MemberKycStatus;
import com.remitro.event.domain.member.enums.MemberLockReason;
import com.remitro.event.domain.member.enums.UnlockActorType;
import com.remitro.event.domain.member.MemberActivatedEvent;
import com.remitro.event.domain.member.MemberCreatedEvent;
import com.remitro.event.domain.member.MemberDormantEvent;
import com.remitro.event.domain.member.MemberKycRejectedEvent;
import com.remitro.event.domain.member.MemberKycRequestedEvent;
import com.remitro.event.domain.member.MemberKycVerifiedEvent;
import com.remitro.event.domain.member.MemberLockedEvent;
import com.remitro.event.domain.member.MemberPasswordChangedEvent;
import com.remitro.event.domain.member.MemberProfileUpdatedEvent;
import com.remitro.event.domain.member.MemberRoleChangedEvent;
import com.remitro.event.domain.member.MemberUnlockedEvent;
import com.remitro.member.domain.member.enums.ActivityStatus;
import com.remitro.member.domain.kyc.enums.KycStatus;
import com.remitro.member.domain.member.enums.LockReason;
import com.remitro.member.domain.member.model.Member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEventMapper {

	private static MemberActivityStatus mapActivityStatus(ActivityStatus status) {
		return MemberActivityStatus.valueOf(status.name());
	}

	private static MemberKycStatus mapKycStatus(KycStatus status) {
		return MemberKycStatus.valueOf(status.name());
	}

	private static MemberLockReason mapLockReason(LockReason reason) {
		return MemberLockReason.valueOf(reason.name());
	}

	public static MemberCreatedEvent toMemberCreatedEvent(Member member) {
		return new MemberCreatedEvent(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			mapActivityStatus(member.getActivityStatus()),
			mapKycStatus(member.getKycStatus()),
			member.getCreatedAt()
		);
	}

	public static MemberLockedEvent toMemberLockedEvent(
		Member member,
		Long adminMemberId,
		LockActorType lockActorType,
		LockReason lockReason,
		LocalDateTime occurredAt
	) {
		return new MemberLockedEvent(
			member.getId(),
			adminMemberId,
			lockActorType,
			mapLockReason(lockReason),
			occurredAt
		);
	}

	public static MemberUnlockedEvent toMemberUnlockedEvent(
		Member member,
		Long adminMemberId,
		UnlockActorType unlockActorType,
		LocalDateTime occurredAt
	) {
		return new MemberUnlockedEvent(
			member.getId(),
			adminMemberId,
			unlockActorType,
			occurredAt
		);
	}

	public static MemberDormantEvent toMemberDormantEvent(Member member, LocalDateTime occurredAt) {
		return new MemberDormantEvent(
			member.getId(),
			occurredAt
		);
	}

	public static MemberActivatedEvent toMemberActivatedEvent(Member member, LocalDateTime occurredAt) {
		return new MemberActivatedEvent(
			member.getId(),
			occurredAt
		);
	}

	public static MemberKycRequestedEvent toMemberKycRequestedEvent(Member member, LocalDateTime occurredAt) {
		return new MemberKycRequestedEvent(
			member.getId(),
			occurredAt
		);
	}

	public static MemberKycVerifiedEvent toMemberKycVerifiedEvent(
		Member member,
		Long adminMemberId,
		LocalDateTime occurredAt
	) {
		return new MemberKycVerifiedEvent(
			member.getId(),
			adminMemberId,
			occurredAt
		);
	}

	public static MemberKycRejectedEvent toMemberKycRejectedEvent(
		Member member,
		Long adminMemberId,
		String reason,
		LocalDateTime occurredAt
	) {
		return new MemberKycRejectedEvent(
			member.getId(),
			adminMemberId,
			reason,
			occurredAt
		);
	}

	public static MemberRoleChangedEvent toMemberRoleChangedEvent(
		Member member,
		Role previousRole,
		Long adminMemberId,
		LocalDateTime occurredAt
	) {
		return new MemberRoleChangedEvent(
			member.getId(),
			previousRole,
			member.getRole(),
			adminMemberId,
			occurredAt
		);
	}

	public static MemberPasswordChangedEvent toMemberPasswordChangedEvent(Member member, LocalDateTime occurredAt) {
		return new MemberPasswordChangedEvent(
			member.getId(),
			occurredAt
		);
	}

	public static MemberProfileUpdatedEvent toMemberProfileUpdatedEvent(Member member, LocalDateTime occurredAt) {
		return new MemberProfileUpdatedEvent(
			member.getId(),
			member.getNickname(),
			member.getPhoneNumber(),
			occurredAt
		);
	}
}
