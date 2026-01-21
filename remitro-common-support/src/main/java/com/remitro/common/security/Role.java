package com.remitro.common.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

	USER("일반 사용자"),
	ADMIN("관리자"),
	SYSTEM("시스템");

	private final String description;

	public String toAuthority() {
		return "ROLE_" + this.name();
	}
}
