package com.remitro.member.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginSecurityStatus {

	NORMAL("정상"),
	LOCKED("로그인 잠금");

	private final String description;
}
