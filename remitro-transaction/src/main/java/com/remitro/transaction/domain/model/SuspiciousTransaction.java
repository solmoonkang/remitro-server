package com.remitro.transaction.domain.model;

import java.time.LocalDateTime;

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

	private SuspiciousTransaction(Long transactionId, Long memberId, AmlRuleCode amlRuleCode, String reason) {
		this.transactionId = transactionId;
		this.memberId = memberId;
		this.amlRuleCode = amlRuleCode;
		this.suspiciousStatus = SuspiciousStatus.OPEN;
		this.reason = reason;
		this.detectedAt = LocalDateTime.now();
	}
}
