package com.remitro.transaction.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "ACCOUNT_STATUS_HISTORIES", indexes = {
	@Index(name = "idx_account_id", columnList = "account_id"),
	@Index(name = "idx_event_id", columnList = "event_id", unique = true)
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountStatusHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_status_history_id", nullable = false)
	private Long id;

	@Column(name = "account_id", nullable = false)
	private Long accountId;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "event_id", unique = true, nullable = false)
	private String eventId;

	@Column(name = "previous_status", length = 30)
	private String previousStatus;

	@Column(name = "new_status", nullable = false, length = 30)
	private String newStatus;

	@Column(name = "actor_type", nullable = false, length = 20)
	private String actorType;

	@Column(name = "reason_code", nullable = false, length = 50)
	private String reasonCode;

	@Column(name = "occurred_at", nullable = false)
	private LocalDateTime occurredAt;

	@Column(name = "received_at", nullable = false)
	private LocalDateTime receivedAt;

	private AccountStatusHistory(
		Long accountId,
		Long memberId,
		String eventId,
		String previousStatus,
		String newStatus,
		String actorType,
		String reasonCode,
		LocalDateTime occurredAt,
		LocalDateTime receivedAt
	) {
		this.accountId = accountId;
		this.memberId = memberId;
		this.eventId = eventId;
		this.previousStatus = previousStatus;
		this.newStatus = newStatus;
		this.actorType = actorType;
		this.reasonCode = reasonCode;
		this.occurredAt = occurredAt;
		this.receivedAt = receivedAt;
	}

	public static AccountStatusHistory create(
		Long accountId,
		Long memberId,
		String eventId,
		String previousStatus,
		String newStatus,
		String actorType,
		String reasonCode,
		LocalDateTime occurredAt
	) {
		return new AccountStatusHistory(
			accountId,
			memberId,
			eventId,
			previousStatus,
			newStatus,
			actorType,
			reasonCode,
			occurredAt,
			LocalDateTime.now()
		);
	}
}
