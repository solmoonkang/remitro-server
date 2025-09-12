package com.remitro.transaction.domain.model;

import static com.remitro.transaction.domain.model.enums.TransactionType.*;

import java.time.LocalDateTime;

import com.remitro.account.domain.model.Account;
import com.remitro.transaction.domain.model.enums.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "TRANSACTIONS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_account_id")
	private Account senderAccount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_account_id")
	private Account receiverAccount;

	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_type", nullable = false)
	private TransactionType transactionType;

	@Column(name = "amount")
	private Long amount;

	@Column(name = "balance_snapshot")
	private Long balanceSnapshot;

	@Column(name = "transaction_at")
	private LocalDateTime transactionAt;

	private Transaction(Account senderAccount, Account receiverAccount, TransactionType transactionType, Long amount,
		Long balanceSnapshot) {

		this.senderAccount = senderAccount;
		this.receiverAccount = receiverAccount;
		this.transactionType = transactionType;
		this.amount = amount;
		this.balanceSnapshot = balanceSnapshot;
		this.transactionAt = LocalDateTime.now();
	}

	public static Transaction createSenderTransaction(Account senderAccount, Account receiverAccount, Long amount) {
		return new Transaction(senderAccount, receiverAccount, TRANSFER, amount, senderAccount.getBalance());
	}

	public static Transaction createReceiverTransaction(Account senderAccount, Account receiverAccount, Long amount) {
		return new Transaction(senderAccount, receiverAccount, TRANSFER, amount, receiverAccount.getBalance());
	}

	public static Transaction createDepositTransaction(Account receiverAccount, Long amount) {
		return new Transaction(null, receiverAccount, DEPOSIT, amount, receiverAccount.getBalance());
	}

	public static Transaction createWithdrawalTransaction(Account senderAccount, Long amount) {
		return new Transaction(senderAccount, null, WITHDRAWAL, amount, senderAccount.getBalance());
	}

	public boolean isSentBy(Long memberId) {
		return this.senderAccount != null && this.receiverAccount.getMember().getId().equals(memberId);
	}

	public boolean isReceivedBy(Long memberId) {
		return this.receiverAccount != null && this.senderAccount.getMember().getId().equals(memberId);
	}
}
