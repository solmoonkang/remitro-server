package com.remitroserver.api.domain.account.entity;

import com.remitroserver.api.domain.account.model.AccountType;
import com.remitroserver.api.domain.account.model.Money;
import com.remitroserver.api.domain.account.model.Status;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.global.common.entity.BaseTimeEntity;

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

	@Column(name = "account_number", length = 30, unique = true, nullable = false)
	private String accountNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Embedded
	@Column(name = "balance", nullable = false)
	private Money balance;

	@Enumerated(EnumType.STRING)
	@Column(name = "account_type", length = 30, nullable = false)
	private AccountType accountType;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 30, nullable = false)
	private Status status;

	private Account(String accountNumber, Member member, Money balance, AccountType accountType, Status status) {
		this.accountNumber = accountNumber;
		this.member = member;
		this.balance = balance;
		this.accountType = accountType;
		this.status = status;
	}

	public static Account create(String accountNumber, Member member, AccountType accountType) {
		return new Account(accountNumber, member, Money.zero(), accountType, Status.ACTIVE);
	}

	public void deposit(Money amount) {
		this.balance = this.balance.add(amount);
	}

	public void withdraw(Money amount) {
		this.balance = this.balance.subtract(amount);
	}
}
