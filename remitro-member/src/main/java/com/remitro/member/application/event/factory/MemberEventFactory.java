package com.remitro.member.application.event.factory;

import com.remitro.event.domain.member.MemberLockedEvent;
import com.remitro.event.domain.member.MemberSignedUpEvent;
import com.remitro.event.domain.member.MemberUnlockedEvent;
import com.remitro.event.domain.member.MemberWithdrawnEvent;
import com.remitro.event.domain.member.enums.MemberLockReason;
import com.remitro.member.domain.member.model.Member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberEventFactory {

	public static MemberSignedUpEvent createSignedUpEvent(Member member) {
		return new MemberSignedUpEvent(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			member.getPhoneNumber()
		);
	}

	public static MemberLockedEvent createLockedEvent(Member member) {
		return new MemberLockedEvent(
			member.getId(),
			MemberLockReason.valueOf(member.getLockReason().name())
		);
	}

	public static MemberUnlockedEvent createUnlockedEvent(Member member) {
		return new MemberUnlockedEvent(
			member.getId()
		);
	}

	public static MemberWithdrawnEvent createWithdrawnEvent(Member member) {
		return new MemberWithdrawnEvent(
			member.getId()
		);
	}
}
