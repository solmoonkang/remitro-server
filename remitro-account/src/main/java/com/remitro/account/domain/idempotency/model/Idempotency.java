package com.remitro.account.domain.idempotency.model;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Idempotency {

	private String requestId;

	private Long memberId;

	private LocalDateTime createdAt;

	private Idempotency(String requestId, Long memberId, LocalDateTime createdAt) {
		this.requestId = requestId;
		this.memberId = memberId;
		this.createdAt = createdAt;
	}

	public static Idempotency issue(String requestId, Long memberId, LocalDateTime now) {
		return new Idempotency(requestId, memberId, now);
	}
}
