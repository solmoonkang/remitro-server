package com.remitroserver.api.domain.transaction.entity;

import java.util.Objects;

import com.remitroserver.api.domain.transaction.model.TransactionStatus;
import com.remitroserver.global.common.entity.BaseTimeEntity;

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

@Entity
@Table(name = "transaction_status_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransactionStatusLog extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_status_log_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transaction_id", nullable = false)
	private Transaction transaction;

	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_status", nullable = false, length = 20)
	private TransactionStatus status;

	private TransactionStatusLog(Transaction transaction, TransactionStatus status) {
		this.transaction = transaction;
		this.status = status;
	}

	public static TransactionStatusLog create(Transaction transaction, TransactionStatus status) {
		return new TransactionStatusLog(
			Objects.requireNonNull(transaction),
			Objects.requireNonNull(status)
		);
	}
}
