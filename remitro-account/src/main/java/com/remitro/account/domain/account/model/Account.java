package com.remitro.account.domain.account.model;

import com.remitro.account.domain.account.enums.ProductType;
import com.remitro.account.domain.status.enums.AccountStatus;
import com.remitro.account.infrastructure.persistence.base.BaseTimeEntity;

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

	@Column(name = "account_number", nullable = false, unique = true, length = 20)
	private String accountNumber;

	@Column(name = "account_name", length = 50)
	private String accountName;

	@Column(name = "balance", nullable = false)
	private Long balance;

	@Column(name = "hashed_pin", nullable = false)
	private String hashedPin;

	@Enumerated(EnumType.STRING)
	@Column(name = "product_type", nullable = false, length = 20)
	private ProductType productType;

	@Enumerated(EnumType.STRING)
	@Column(name = "account_status", nullable = false, length = 20)
	private AccountStatus accountStatus;

	private Account(
		Long memberId,
		String accountNumber,
		String accountName,
		String hashedPin,
		ProductType productType
	) {
		this.memberId = memberId;
		this.accountNumber = accountNumber;
		this.accountName = accountName;
		this.balance = 0L;
		this.hashedPin = hashedPin;
		this.productType = productType;
		this.accountStatus = AccountStatus.NORMAL;
	}

	public static Account create(
		Long memberId,
		String accountNumber,
		String accountName,
		String hashedPin,
		ProductType productType
	) {
		return new Account(
			memberId,
			accountNumber,
			accountName,
			hashedPin,
			productType
		);
	}

	public void increaseBalance(long amount) {
		this.balance += amount;
	}

	public void decreaseBalance(long amount) {
		this.balance -= amount;
	}

	public void rename(String newAccountName) {
		this.accountName = newAccountName;
	}
}
