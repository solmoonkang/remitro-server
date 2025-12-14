package com.remitro.transaction.domain.aml.model;

import java.time.LocalDateTime;

import com.remitro.common.error.exception.ConflictException;
import com.remitro.transaction.domain.enums.AmlRuleCode;
import com.remitro.transaction.domain.enums.SuspiciousStatus;
import com.remitro.transaction.infrastructure.BaseTimeEntity;

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
@Table(name = "SUSPICIOUS_TRANSACTIONS", indexes = {
	@Index(name = "idx_suspicious_member_id", columnList = "member_id"),
	@Index(name = "idx_suspicious_account_id", columnList = "account_id"),
	@Index(name = "idx_suspicious_status", columnList = "suspicious_status")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SuspiciousTransaction extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "suspicious_transaction_id", nullable = false)
	private Long id;

	@Column(name = "transaction_id", nullable = false)
	private Long transactionId;

	@Column(name = "account_id", nullable = false)
	private Long accountId;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Enumerated(EnumType.STRING)
	@Column(name = "aml_rule_code", nullable = false, length = 50)
	private AmlRuleCode amlRuleCode;

	@Enumerated(EnumType.STRING)
	@Column(name = "suspicious_status", nullable = false, length = 20)
	private SuspiciousStatus suspiciousStatus;

	@Column(name = "reason", nullable = false)
	private String reason;

	@Column(name = "detected_at", nullable = false)
	private LocalDateTime detectedAt;

	@Column(name = "handled_at")
	private LocalDateTime handledAt;

	private SuspiciousTransaction(
		Long transactionId,
		Long accountId,
		Long memberId,
		AmlRuleCode amlRuleCode,
		String reason
	) {
		this.transactionId = transactionId;
		this.accountId = accountId;
		this.memberId = memberId;
		this.amlRuleCode = amlRuleCode;
		this.suspiciousStatus = SuspiciousStatus.OPEN;
		this.reason = reason;
		this.detectedAt = LocalDateTime.now();
		this.handledAt = null;
	}

	public static SuspiciousTransaction open(
		Long transactionId,
		Long accountId,
		Long memberId,
		AmlRuleCode amlRuleCode,
		String reason
	) {
		return new SuspiciousTransaction(transactionId, accountId, memberId, amlRuleCode, reason);
	}

	public void markReviewed() {
		if (this.suspiciousStatus != SuspiciousStatus.OPEN) {
			throw new ConflictException("OPEN 상태만 REVIEWED로 변경할 수 있습니다.");
		}

		this.suspiciousStatus = SuspiciousStatus.REVIEWED;
		this.handledAt = LocalDateTime.now();
	}

	public void markClosed() {
		if (this.suspiciousStatus == SuspiciousStatus.CLOSED) {
			return;
		}

		this.suspiciousStatus = SuspiciousStatus.CLOSED;
		this.handledAt = LocalDateTime.now();
	}
}
