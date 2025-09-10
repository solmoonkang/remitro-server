package com.remitro.account.domain.model;

import com.remitro.account.domain.model.enums.AccountStatus;
import com.remitro.account.domain.model.enums.AccountType;
import com.remitro.common.common.entity.BaseTimeEntity;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.model.ErrorMessage;
import com.remitro.member.domain.model.Member;

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
@Table(name = "ACCOUNTS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "account_number", unique = true, nullable = false)
	private String accountNumber;

	@Column(name = "account_name", unique = true, length = 50)
	private String accountName;

	@Column(name = "balance", nullable = false)
	private Long balance;

	@Column(name = "password", nullable = false, length = 4)
	private String password;

	@Column(name = "is_activated", nullable = false)
	private boolean isActivated;

	@Enumerated(EnumType.STRING)
	@Column(name = "account_type", nullable = false)
	private AccountType accountType;

	@Enumerated(EnumType.STRING)
	@Column(name = "account_status", nullable = false)
	private AccountStatus accountStatus;

	private Account(Member member, String accountNumber, String accountName, String password, AccountType accountType) {
		this.member = member;
		this.accountNumber = accountNumber;
		this.accountName = accountName;
		this.balance = 0L;
		this.password = password;
		this.isActivated = true;
		this.accountType = accountType;
		this.accountStatus = AccountStatus.NORMAL;
	}

	public static Account createAccount(Member member, String accountNumber, String accountName, String password,
		AccountType accountType) {
		return new Account(member, accountNumber, accountName, password, accountType);
	}

	public void deposit(Long amount) {
		this.balance += amount;
	}

	public void withdraw(Long amount) {
		if (this.balance < amount) {
			throw new BadRequestException(ErrorMessage.INSUFFICIENT_FUNDS);
		}

		this.balance -= amount;
	}

	public void updateAccountStatus(AccountStatus accountStatus) {
		if (this.accountStatus == AccountStatus.TERMINATED) {
			throw new BadRequestException(ErrorMessage.INVALID_STATUS_CHANGE);
		}

		this.accountStatus = accountStatus;
	}
}
