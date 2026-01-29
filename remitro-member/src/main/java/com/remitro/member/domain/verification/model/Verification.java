package com.remitro.member.domain.verification.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Verification {

	private String email;

	private String verificationCode;

	private String verificationToken;

	private boolean isVerified;

	private LocalDateTime expiresAt;

	private Verification(String email, String verificationCode, LocalDateTime expiresAt) {
		this.email = email;
		this.verificationCode = verificationCode;
		this.verificationToken = UUID.randomUUID().toString();
		this.isVerified = false;
		this.expiresAt = expiresAt;
	}

	public static Verification issue(String email, String verificationCode, LocalDateTime expiresAt) {
		return new Verification(email, verificationCode, expiresAt);
	}

	public void confirm() {
		this.isVerified = true;
	}
}
