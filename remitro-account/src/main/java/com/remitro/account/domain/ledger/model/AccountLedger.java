package com.remitro.account.domain.ledger.model;

import org.hibernate.annotations.Comment;

import com.remitro.account.domain.common.BaseTimeEntity;
import com.remitro.account.domain.ledger.enums.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
	name = "ACCOUNT_LEDGERS",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_ledger_transaction_id", columnNames = "transaction_id")
	},
	indexes = {
		@Index(name = "idx_ledger_account_id_created_at", columnList = "account_id, created_at")
	}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountLedger extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_ledger_id")
	private Long id;

	@Comment("계좌 ID")
	@Column(name = "account_id", nullable = false)
	private Long accountId;

	@Comment("멱등성 ID")
	@Column(name = "request_id", nullable = false, length = 100)
	private String requestId;

	@Comment("거래 유형")
	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_type", nullable = false, length = 20)
	private TransactionType transactionType;

	@Comment("거래 금액")
	@Column(name = "amount", nullable = false)
	private Long amount;

	@Comment("거래 후 잔액 스냅샷")
	@Column(name = "balance_snapshot", nullable = false)
	private Long balanceSnapshot;

	@Comment("거래 내용")
	@Column(name = "description", length = 100)
	private String description;

	private AccountLedger(
		Long accountId,
		String requestId,
		TransactionType transactionType,
		Long amount,
		Long balanceSnapshot,
		String description
	) {
		this.accountId = accountId;
		this.requestId = requestId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.balanceSnapshot = balanceSnapshot;
		this.description = description;
	}

	public static AccountLedger record(
		Long accountId,
		String requestId,
		TransactionType transactionType,
		Long amount,
		Long balanceSnapshot,
		String description
	) {
		return new AccountLedger(
			accountId,
			requestId,
			transactionType,
			amount,
			balanceSnapshot,
			description
		);
	}
}
