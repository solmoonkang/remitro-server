package com.remitro.member.domain.history.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusType {

	MEMBER_STATUS("회원 운영 상태"),
	LOGIN_SECURITY_STATUS("로그인 보안 상태");

	private final String description;
}
