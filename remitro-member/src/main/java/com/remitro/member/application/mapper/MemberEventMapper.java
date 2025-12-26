package com.remitro.member.application.mapper;

import java.time.LocalDateTime;

import com.remitro.common.security.Role;
import com.remitro.event.member.enums.MemberActivityStatus;
import com.remitro.event.member.enums.MemberKycStatus;
import com.remitro.event.member.enums.MemberLockReason;
import com.remitro.event.member.enums.UnlockActorType;
import com.remitro.event.member.event.MemberActivatedEvent;
import com.remitro.event.member.event.MemberCreatedEvent;
import com.remitro.event.member.event.MemberDormantEvent;
import com.remitro.event.member.event.MemberKycRejectedEvent;
import com.remitro.event.member.event.MemberKycRequestedEvent;
import com.remitro.event.member.event.MemberKycVerifiedEvent;
import com.remitro.event.member.event.MemberLockedEvent;
import com.remitro.event.member.event.MemberRoleChangedEvent;
import com.remitro.event.member.event.MemberUnlockedEvent;
import com.remitro.member.domain.enums.ActivityStatus;
import com.remitro.member.domain.enums.KycStatus;
import com.remitro.member.domain.enums.LockReason;
import com.remitro.member.domain.model.Member;

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

	public static MemberLockedEvent toMemberLockedEvent(Member member, Long adminMemberId) {
		return new MemberLockedEvent(
			member.getId(),
			adminMemberId,
			mapLockReason(member.getLockReason()),
			member.getLockedAt()
		);
	}

	public static MemberUnlockedEvent toMemberUnlockedEventByAdmin(
		Member member,
		Long adminMemberId,
		LocalDateTime unlockedAt
	) {
		return new MemberUnlockedEvent(
			member.getId(),
			adminMemberId,
			UnlockActorType.ADMIN,
			unlockedAt
		);
	}

	public static MemberUnlockedEvent toMemberUnlockedEventBySelf(
		Member member,
		LocalDateTime unlockedAt
	) {
		return new MemberUnlockedEvent(
			member.getId(),
			null,
			UnlockActorType.SELF,
			unlockedAt
		);
	}

	public static MemberDormantEvent toMemberDormantEvent(Member member, LocalDateTime dormantAt) {
		return new MemberDormantEvent(
			member.getId(),
			dormantAt
		);
	}

	public static MemberActivatedEvent toMemberActivatedEvent(Member member, LocalDateTime activatedAt) {
		return new MemberActivatedEvent(
			member.getId(),
			activatedAt
		);
	}

	public static MemberKycRequestedEvent toMemberKycRequestedEvent(Member member, LocalDateTime requestedAt) {
		return new MemberKycRequestedEvent(
			member.getId(),
			requestedAt
		);
	}

	public static MemberKycVerifiedEvent toMemberKycVerifiedEvent(
		Member member,
		Long adminMemberId,
		LocalDateTime verifiedAt
	) {
		return new MemberKycVerifiedEvent(
			member.getId(),
			adminMemberId,
			verifiedAt
		);
	}

	public static MemberKycRejectedEvent toMemberKycRejectedEvent(
		Member member,
		Long adminMemberId,
		String reason,
		LocalDateTime rejectedAt
	) {
		return new MemberKycRejectedEvent(
			member.getId(),
			adminMemberId,
			reason,
			rejectedAt
		);
	}

	public static MemberRoleChangedEvent toMemberRoleChangedEvent(
		Member member,
		Role previousRole,
		Long adminMemberId,
		LocalDateTime changedAt
	) {
		return new MemberRoleChangedEvent(
			member.getId(),
			previousRole,
			member.getRole(),
			adminMemberId,
			changedAt
		);
	}
}
