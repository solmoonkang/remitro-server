package com.remitro.member.domain.verification.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@RedisHash(value = "VERIFICATION_TOKEN", timeToLive = 300)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Verification {

	@Id
	private String email;

	private String verificationCode;

	@Indexed
	private String verificationToken;

	private boolean isVerified;

	private Verification(String email, String verificationCode) {
		this.email = email;
		this.verificationCode = verificationCode;
		this.verificationToken = UUID.randomUUID().toString();
		this.isVerified = false;
	}

	public static Verification issue(String email, String verificationCode) {
		return new Verification(email, verificationCode);
	}

	public void confirm() {
		this.isVerified = true;
	}
}
