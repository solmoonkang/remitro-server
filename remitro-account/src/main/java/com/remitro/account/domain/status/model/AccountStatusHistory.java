package com.remitro.account.domain.status.model;

import java.time.LocalDateTime;

import com.remitro.account.domain.status.enums.AccountStatus;
import com.remitro.account.domain.status.enums.StatusChangeReason;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	@Index(name = "idx_account_status_histories_account_id", columnList = "account_id"),
	@Index(name = "idx_account_status_histories_occurred_at", columnList = "occurred_at")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountStatusHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_status_history_id")
	private Long id;

	@Column(name = "account_id", nullable = false)
	private Long accountId;

	@Enumerated(EnumType.STRING)
	@Column(name = "previous_status", nullable = false, length = 20)
	private AccountStatus previousStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "new_status", nullable = false, length = 20)
	private AccountStatus newStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "status_change_reason", nullable = false, length = 30)
	private StatusChangeReason statusChangeReason;

	@Column(name = "occurred_at", nullable = false)
	private LocalDateTime occurredAt;

	private AccountStatusHistory(
		Long accountId,
		AccountStatus previousStatus,
		AccountStatus newStatus,
		StatusChangeReason statusChangeReason,
		LocalDateTime occurredAt
	) {
		this.accountId = accountId;
		this.previousStatus = previousStatus;
		this.newStatus = newStatus;
		this.statusChangeReason = statusChangeReason;
		this.occurredAt = occurredAt;
	}

	public static AccountStatusHistory record(
		Long accountId,
		AccountStatus previousStatus,
		AccountStatus newStatus,
		StatusChangeReason statusChangeReason,
		LocalDateTime occurredAt
	) {
		return new AccountStatusHistory(
			accountId,
			previousStatus,
			newStatus,
			statusChangeReason,
			occurredAt
		);
	}
}
