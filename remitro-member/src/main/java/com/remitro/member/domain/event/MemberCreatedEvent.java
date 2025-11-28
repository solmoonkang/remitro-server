package com.remitro.member.domain.event;

public record MemberCreatedEvent(
	Long id,

	String email,

	String nickname
) {
}
