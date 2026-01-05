package com.remitro.member.application.event;

import com.remitro.event.domain.member.MemberSignedUpEvent;
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
}
