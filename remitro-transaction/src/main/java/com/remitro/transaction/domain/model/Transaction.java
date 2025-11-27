package com.remitro.transaction.domain.model;

import java.time.LocalDateTime;

import com.remitro.transaction.domain.model.enums.TransactionStatus;
import com.remitro.transaction.domain.model.enums.TransactionType;

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
@Table(name = "TRANSACTIONS", indexes = {
	@Index(name = "idx_transaction_event_id", columnList = "event_id", unique = true)
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id", nullable = false)
	private Long id;

	@Column(name = "account_id", nullable = false)
	private Long accountId;

	@Column(name = "event_id", unique = true, nullable = false, length = 36)
	private String eventId;

	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_type", nullable = false, length = 20)
	private TransactionType transactionType;

	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_status", nullable = false, length = 20)
	private TransactionStatus transactionStatus;

	@Column(name = "amount", nullable = false)
	private Long amount;

	@Column(name = "description")
	private String description;

	@Column(name = "occurred_at", nullable = false)
	private LocalDateTime occurredAt;

	private Transaction(
		Long accountId,
		String eventId,
		TransactionType transactionType,
		Long amount,
		String description,
		LocalDateTime occurredAt
	) {
		this.accountId = accountId;
		this.eventId = eventId;
		this.transactionType = transactionType;
		this.transactionStatus = TransactionStatus.COMPLETED;
		this.amount = amount;
		this.description = description;
		this.occurredAt = occurredAt;
	}

	public static Transaction create(
		Long accountId,
		String eventId,
		TransactionType transactionType,
		Long amount,
		String description,
		LocalDateTime occurredAt
	) {
		return new Transaction(
			accountId,
			eventId,
			transactionType,
			amount,
			description,
			occurredAt
		);
	}
}
