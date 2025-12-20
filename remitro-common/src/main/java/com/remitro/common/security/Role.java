package com.remitro.common.security;

public enum Role {

	MEMBER,			// 일반 사용자
	ADMIN,			// 운영자
	SYSTEM;			// 내부 서비스 / 배치 / 마이그레이션

	public boolean isAdmin() {
		return this == ADMIN;
	}

	public boolean isSystem() {
		return this == SYSTEM;
	}
}
