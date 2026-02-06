package com.remitro.member.application.mapper;

import java.time.LocalDateTime;

import com.remitro.event.domain.member.enums.MemberLifecycleStatus;
import com.remitro.event.domain.member.model.MemberRegisteredEvent;
import com.remitro.member.domain.member.model.Member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventMapper {

	public static MemberRegisteredEvent toMemberRegisteredEvent(Member member, LocalDateTime now) {
		return new MemberRegisteredEvent(
			member.getId(),
			member.getNickname(),
			MemberLifecycleStatus.ACTIVE,
			member.getCreatedAt()
		);
	}
}
