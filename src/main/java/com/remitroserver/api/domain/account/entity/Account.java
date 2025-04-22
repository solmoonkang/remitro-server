package com.remitroserver.api.domain.account.entity;

import static com.remitroserver.api.domain.account.model.AccountStatus.*;
import static com.remitroserver.global.error.model.ErrorMessage.*;

import java.util.Objects;
import java.util.UUID;

import com.remitroserver.api.domain.account.model.AccountStatus;
import com.remitroserver.api.domain.account.model.AccountType;
import com.remitroserver.api.domain.account.model.Money;
import com.remitroserver.api.domain.member.entity.Member;
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
@Table(name = "accounts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id", nullable = false)
	private Long id;

	@Column(name = "account_number", unique = true, nullable = false, length = 30)
	private String accountNumber;

	@Column(name = "account_token", updatable = false, unique = true, nullable = false)
	private UUID accountToken;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Embedded
	@AttributeOverride(
		name = "value",
		column = @Column(name = "balance", nullable = false)
	)
	private Money balance;

	@Enumerated(EnumType.STRING)
	@Column(name = "account_type", nullable = false, length = 30)
	private AccountType accountType;

	@Enumerated(EnumType.STRING)
	@Column(name = "account_status", nullable = false, length = 30)
	private AccountStatus status;

	private Account(String accountNumber, Member member, Money balance, AccountType accountType, AccountStatus status) {
		this.accountNumber = accountNumber;
		this.member = member;
		this.balance = balance;
		this.accountType = accountType;
		this.status = status;
		this.accountToken = UUID.randomUUID();
	}

	public static Account create(String accountNumber, Member member, AccountType accountType) {
		return new Account(
			Objects.requireNonNull(accountNumber),
			Objects.requireNonNull(member),
			Objects.requireNonNull(Money.zero()),
			Objects.requireNonNull(accountType),
			Objects.requireNonNull(ACTIVE)
		);
	}

	public void deposit(Money amount) {
		this.balance = this.balance.add(amount);
	}

	public void withdraw(Money amount) {
		this.balance = this.balance.subtract(amount);
	}

	public void changeStatusTo(AccountStatus targetStatus) {
		if (this.status == ACTIVE && targetStatus == AccountStatus.CLOSED && !this.balance.isZero()) {
			throw new BadRequestException(ACCOUNT_BALANCE_NOT_ZERO_ERROR);
		}

		this.status = this.status.transitionTo(targetStatus);
	}

	public void validateIsActive() {
		if (this.status != ACTIVE) {
			throw new BadRequestException(ACCOUNT_NOT_ACTIVE_ERROR);
		}
	}

	public boolean isSameAccount(Account target) {
		return this.id != null && this.id.equals(target.getId());
	}
}
