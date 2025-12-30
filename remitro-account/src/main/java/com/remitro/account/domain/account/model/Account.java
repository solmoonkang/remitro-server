package com.remitro.account.domain.account.model;

import java.time.LocalDateTime;

import com.remitro.account.domain.account.enums.LifecycleStatus;
import com.remitro.account.domain.common.model.BaseTimeEntity;
import com.remitro.account.domain.product.enums.ProductType;

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

	@Column(name = "hashed_pin", nullable = false)
	private String hashedPin;

	@Enumerated(EnumType.STRING)
	@Column(name = "account_type", nullable = false)
	private ProductType productType;

	@Enumerated(EnumType.STRING)
	@Column(name = "account_status", nullable = false)
	private LifecycleStatus lifecycleStatus;

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
		this.lifecycleStatus = LifecycleStatus.NORMAL;
	}

	public static Account create(
		Long memberId,
		String accountNumber,
		String accountName,
		String hashedPin,
		ProductType productType
	) {
		return new Account(memberId, accountNumber, accountName, hashedPin, productType);
	}

	public static Account createLoan(Long memberId, String accountNumber, ProductType productType) {
		return new Account(memberId, accountNumber, "LOAN_ACCOUNT", null, productType);
	}

	public static Account createVirtual(Long memberId, String accountNumber, LocalDateTime expiredAt) {
		return new Account(memberId, accountNumber, "VIRTUAL_ACCOUNT", null, ProductType.VIRTUAL);
	}
}
