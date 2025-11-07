package com.remitro.transaction.domain.model;

import static com.remitro.transaction.domain.model.enums.TransactionType.*;

import java.time.LocalDateTime;

import com.remitro.account.domain.model.Account;
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
	@Index(name = "idx_sender_account_id", columnList = "sender_account_id"),
	@Index(name = "idx_receiver_account_id", columnList = "receiver_account_id")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id", unique = true, nullable = false)
	private Long id;

	@Column(name = "event_id", unique = true, nullable = false, length = 36)
	private String eventId;

	@Column(name = "sender_account_id")
	private Long senderAccountId;

	@Column(name = "receiver_account_id")
	private Long receiverAccountId;

	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_type", nullable = false)
	private TransactionType transactionType;

	@Column(name = "amount")
	private Long amount;

	@Column(name = "sender_balance_snapshot")
	private Long senderBalanceSnapshot;

	@Column(name = "receiver_balance_snapshot")
	private Long receiverBalanceSnapshot;

	@Column(name = "transaction_at")
	private LocalDateTime transactionAt;

	private Transaction(String eventId, Long senderAccountId, Long receiverAccountId, TransactionType transactionType,
		Long amount, Long senderBalanceSnapshot, Long receiverBalanceSnapshot) {

		this.eventId = eventId;
		this.senderAccountId = senderAccountId;
		this.receiverAccountId = receiverAccountId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.senderBalanceSnapshot = senderBalanceSnapshot;
		this.receiverBalanceSnapshot = receiverBalanceSnapshot;
		this.transactionAt = LocalDateTime.now();
	}

	public static Transaction createSenderTransaction(Account senderAccount, Account receiverAccount, Long amount,
		String idempotencyKey) {

		return new Transaction(senderAccount, receiverAccount, TRANSFER, amount, senderAccount.getBalance(),
			idempotencyKey);
	}

	public static Transaction createReceiverTransaction(Account senderAccount, Account receiverAccount, Long amount,
		String idempotencyKey) {

		return new Transaction(senderAccount, receiverAccount, TRANSFER, amount, receiverAccount.getBalance(),
			idempotencyKey);
	}

	public static Transaction createDepositTransaction(Account receiverAccount, Long amount, String idempotencyKey) {
		return new Transaction(null, receiverAccount, DEPOSIT, amount, receiverAccount.getBalance(), idempotencyKey);
	}

	public static Transaction createWithdrawalTransaction(Account senderAccount, Long amount, String idempotencyKey) {
		return new Transaction(senderAccount, null, WITHDRAWAL, amount, senderAccount.getBalance(), idempotencyKey);
	}

	public boolean isSentBy(Long memberId) {
		return this.senderAccount != null && this.receiverAccount.getMember().getId().equals(memberId);
	}

	public boolean isReceivedBy(Long memberId) {
		return this.receiverAccount != null && this.senderAccount.getMember().getId().equals(memberId);
	}
}
