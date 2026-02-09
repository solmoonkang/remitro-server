package com.remitro.account.domain.account.model;

import java.util.Optional;

import org.hibernate.annotations.Comment;

import com.remitro.account.domain.account.enums.AccountStatus;
import com.remitro.account.domain.account.enums.AccountType;
import com.remitro.account.domain.common.BaseTimeEntity;
import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.BadRequestException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
	name = "ACCOUNTS",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_account_number", columnNames = "account_number")
	}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private Long id;

	@Comment("회원 ID")
	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Comment("계좌 번호")
	@Column(name = "account_number", nullable = false, length = 30)
	private String accountNumber;

	@Comment("계좌 별칭")
	@Column(name = "account_alias", nullable = false, length = 30)
	private String accountAlias;

	@Comment("계좌 잔액")
	@Column(name = "balance", nullable = false)
	private Long balance;

	@Comment("계좌 상태")
	@Enumerated(EnumType.STRING)
	@Column(name = "account_status", nullable = false, length = 20)
	private AccountStatus accountStatus;

	@Comment("계좌 타입")
	@Enumerated(EnumType.STRING)
	@Column(name = "account_type", nullable = false, length = 20)
	private AccountType accountType;

	@Comment("PIN 해시")
	@Column(name = "pin_number_hash", nullable = false)
	private String pinNumberHash;

	private Account(
		Long memberId,
		String accountNumber,
		String accountAlias,
		AccountType accountType,
		String pinNumberHash
	) {
		this.memberId = memberId;
		this.accountNumber = accountNumber;
		this.accountAlias = getOrDefaultAlias(accountAlias, accountType);
		this.balance = 0L;
		this.accountStatus = AccountStatus.ACTIVE;
		this.accountType = accountType;
		this.pinNumberHash = pinNumberHash;
	}

	public static Account open(
		Long memberId,
		String accountNumber,
		String accountAlias,
		AccountType accountType,
		String pinNumberHash
	) {
		return new Account(memberId, accountNumber, accountAlias, accountType, pinNumberHash);
	}

	private String getOrDefaultAlias(String accountAlias, AccountType accountType) {
		return Optional.ofNullable(accountAlias)
			.filter(input -> !input.isBlank())
			.orElseGet(accountType::getDefaultAlias);
	}

	public void deposit(Long amount) {
		if (amount <= 0) {
			throw new BadRequestException(ErrorCode.INVALID_TRANSACTION_AMOUNT);
		}
		this.balance += amount;
	}

	public void withdraw(Long amount) {
		if (this.balance < amount) {
			throw new BadRequestException(ErrorCode.INSUFFICIENT_BALANCE);
		}
		this.balance -= amount;
	}
}
