package com.remitro.common.infra.auth.model;

public record AuthMember(
	Long id,
	String email,
	String nickname
) {
}
