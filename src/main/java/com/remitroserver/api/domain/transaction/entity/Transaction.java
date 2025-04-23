package com.remitroserver.api.domain.transaction.entity;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.domain.account.model.Money;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.domain.transaction.model.TransactionStatus;
import com.remitroserver.global.common.entity.BaseTimeEntity;
import com.remitroserver.global.error.exception.BadRequestException;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

@Entity
@Table(name = "account_transactions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_account_id", nullable = false)
	private Account fromAccount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_account_id", nullable = false)
	private Account toAccount;

	@Column(name = "transaction_token", unique = true, nullable = false, updatable = false)
	private UUID transactionToken;

	@Embedded
	@AttributeOverride(
		name = "value",
		column = @Column(name = "amount", nullable = false)
	)
	private Money amount;

	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_status", nullable = false, length = 20)
	private TransactionStatus status;

	@Column(name = "idempotency_key", unique = true, nullable = false, length = 64)
	private String idempotencyKey;

	private Transaction(Account fromAccount, Account toAccount, Money amount, TransactionStatus status,
		String idempotencyKey) {

		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.transactionToken = UUID.randomUUID();
		this.amount = amount;
		this.status = status;
		this.idempotencyKey = idempotencyKey;
	}

	public static Transaction create(Account from, Account to, Money amount, String idempotencyKey) {
		return new Transaction(
			Objects.requireNonNull(from),
			Objects.requireNonNull(to),
			Objects.requireNonNull(amount),
			Objects.requireNonNull(TransactionStatus.REQUESTED),
			Objects.requireNonNull(idempotencyKey)
		);
	}

	public void complete() {
		this.status = TransactionStatus.COMPLETED;
	}

	public void cancel() {
		if (this.status != TransactionStatus.REQUESTED) {
			throw new BadRequestException(TRANSACTION_NOT_REQUESTED_ERROR);
		}

		this.status = TransactionStatus.CANCELLED;
	}

	public void validateNotExpired(Duration allowedTime) {
		LocalDateTime now = LocalDateTime.now();

		if (Duration.between(this.getCreatedAt(), now).compareTo(allowedTime) > 0) {
			throw new BadRequestException(TRANSACTION_EXPIRED_ERROR);
		}
	}

	public boolean isOwner(Member member) {
		return fromAccount.getMember().equals(member)
			|| toAccount.getMember().equals(member);
	}
}
