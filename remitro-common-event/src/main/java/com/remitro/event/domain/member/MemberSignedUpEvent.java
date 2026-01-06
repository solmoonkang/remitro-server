package com.remitro.event.domain.member;

public record MemberSignedUpEvent(
	Long memberId,

	String email,

	String nickname,

	String phoneNumber
) {
}
