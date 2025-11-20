package com.remitro.transaction.domain.model;

import java.time.LocalDateTime;

import com.remitro.common.domain.BaseTimeEntity;
import com.remitro.transaction.domain.model.enums.LedgerDirection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "LEDGER_ENTRIES", indexes = {
	@Index(name = "idx_ledger_transaction_id", columnList = "transaction_id"),
	@Index(name = "idx_ledger_account_id", columnList = "account_id")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LedgerEntry extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ledger_entry_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transaction_id", nullable = false)
	private Transaction transaction;

	@Enumerated(EnumType.STRING)
	@Column(name = "ledger_direction", nullable = false, length = 10)
	private LedgerDirection ledgerDirection;

	@Column(name = "amount", nullable = false)
	private Long amount;

	@Column(name = "balance_after", nullable = false)
	private Long balanceAfter;

	@Column(name = "occurred_at", nullable = false)
	private LocalDateTime occurredAt;

	private LedgerEntry(
		Transaction transaction,
		LedgerDirection ledgerDirection,
		Long amount,
		Long balanceAfter,
		LocalDateTime occurredAt
	) {
		this.transaction = transaction;
		this.ledgerDirection = ledgerDirection;
		this.amount = amount;
		this.balanceAfter = balanceAfter;
		this.occurredAt = occurredAt;
	}

	public static LedgerEntry create(
		Transaction transaction,
		LedgerDirection ledgerDirection,
		Long amount,
		Long balanceAfter,
		LocalDateTime occurredAt
	) {
		return new LedgerEntry(
			transaction,
			ledgerDirection,
			amount,
			balanceAfter,
			occurredAt
		);
	}
}
