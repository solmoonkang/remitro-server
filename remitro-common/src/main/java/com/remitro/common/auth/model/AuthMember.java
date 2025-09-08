package com.remitro.common.auth.model;

public record AuthMember(
	Long id,
	String email,
	String nickname
) {
}
