package com.remitro.account.domain.model;

import java.time.LocalDateTime;

import com.remitro.account.domain.model.enums.AccountStatus;
import com.remitro.account.domain.model.enums.AccountType;
import com.remitro.common.domain.BaseTimeEntity;

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
@Table(name = "ACCOUNTS", indexes = {
	@Index(name = "idx_member_id", columnList = "member_id"),
	@Index(name = "idx_account_number", columnList = "account_number", unique = true)
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id", unique = true, nullable = false)
	private Long id;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "account_number", unique = true, nullable = false, length = 20)
	private String accountNumber;

	@Column(name = "account_name", length = 50)
	private String accountName;

	@Column(name = "balance", nullable = false)
	private Long balance;

	@Column(name = "hashed_password", nullable = false)
	private String hashedPassword;

	@Enumerated(EnumType.STRING)
	@Column(name = "account_type", nullable = false)
	private AccountType accountType;

	@Enumerated(EnumType.STRING)
	@Column(name = "account_status", nullable = false)
	private AccountStatus accountStatus;

	@Column(name = "last_transaction_at", nullable = false)
	private LocalDateTime lastTransactionAt;

	private Account(
		Long memberId,
		String accountNumber,
		String accountName,
		String hashedPassword,
		AccountType accountType
	) {
		this.memberId = memberId;
		this.accountNumber = accountNumber;
		this.accountName = accountName;
		this.balance = 0L;
		this.hashedPassword = hashedPassword;
		this.accountType = accountType;
		this.accountStatus = AccountStatus.NORMAL;
	}

	public static Account create(
		Long memberId,
		String accountNumber,
		String accountName,
		String hashedPassword,
		AccountType accountType
	) {
		return new Account(memberId, accountNumber, accountName, hashedPassword, accountType);
	}

	public void increaseBalance(Long amount) {
		this.balance += amount;
	}

	public void freeze() {
		this.accountStatus = AccountStatus.FROZEN;
	}

	public void suspend() {
		this.accountStatus = AccountStatus.SUSPENDED;
	}

	public void dormant() {
		this.accountStatus = AccountStatus.DORMANT;
	}

	public void terminate() {
		this.accountStatus = AccountStatus.TERMINATED;
	}
}
