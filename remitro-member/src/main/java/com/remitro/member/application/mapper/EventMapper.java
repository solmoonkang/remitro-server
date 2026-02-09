package com.remitro.member.application.mapper;

import java.time.LocalDateTime;

import com.remitro.event.domain.member.enums.MemberLifecycleStatus;
import com.remitro.event.domain.member.model.MemberProfileUpdatedEvent;
import com.remitro.event.domain.member.model.MemberRegisteredEvent;
import com.remitro.event.domain.member.model.MemberStatusChangedEvent;
import com.remitro.event.domain.member.model.MemberWithdrawnEvent;
import com.remitro.member.domain.audit.model.StatusHistory;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class EventMapper {

	private static MemberLifecycleStatus toLifecycleStatus(MemberStatus memberStatus) {
		return switch (memberStatus) {
			case ACTIVE -> MemberLifecycleStatus.ACTIVE;
			case DORMANT -> MemberLifecycleStatus.DORMANT;
			case SUSPENDED -> MemberLifecycleStatus.SUSPENDED;
			case WITHDRAWN -> MemberLifecycleStatus.WITHDRAWN;
		};
	}

	public static MemberRegisteredEvent toMemberRegisteredEvent(Member member, LocalDateTime now) {
		return new MemberRegisteredEvent(
			member.getId(),
			member.getNickname(),
			toLifecycleStatus(member.getMemberStatus()),
			now
		);
	}

	public static MemberProfileUpdatedEvent toMemberProfileUpdatedEvent(Member member, LocalDateTime now) {
		return new MemberProfileUpdatedEvent(
			member.getId(),
			member.getNickname(),
			now
		);
	}

	public static MemberStatusChangedEvent toMemberStatusChangedEvent(
		Member member,
		StatusHistory statusHistory,
		LocalDateTime changedAt
	) {
		return new MemberStatusChangedEvent(
			member.getId(),
			toLifecycleStatus(member.getMemberStatus()),
			statusHistory.getChangeReason().getDescription(),
			changedAt
		);
	}

	public static MemberWithdrawnEvent toMemberWithdrawnEvent(Member member, LocalDateTime withdrawnAt) {
		return new MemberWithdrawnEvent(
			member.getId(),
			toLifecycleStatus(member.getMemberStatus()),
			withdrawnAt
		);
	}
}
