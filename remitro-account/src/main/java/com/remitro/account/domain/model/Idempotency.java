package com.remitro.account.domain.model;

import com.remitro.account.domain.enums.IdempotencyOperationType;
import com.remitro.account.domain.enums.IdempotencyStatus;
import com.remitro.account.infrastructure.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "IDEMPOTENCY", indexes = {
	@Index(name = "idx_idempotency_operation_type", columnList = "idempotencyOperationType"),
	@Index(name = "idx_idempotency_member", columnList = "memberId"),
	@Index(name = "idx_idempotency_resource", columnList = "resourceId")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Idempotency extends BaseTimeEntity {

	@Id
	@Column(name = "idempotency_key", nullable = false, length = 100)
	private String idempotencyKey;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "resource_id")
	private Long resourceId;

	@Enumerated(EnumType.STRING)
	@Column(name = "idempotency_operation_type", nullable = false, length = 30)
	private IdempotencyOperationType idempotencyOperationType;

	@Enumerated(EnumType.STRING)
	@Column(name = "idempotency_status")
	private IdempotencyStatus idempotencyStatus;

	private Idempotency(
		String idempotencyKey,
		Long memberId,
		IdempotencyOperationType idempotencyOperationType
	) {
		this.idempotencyKey = idempotencyKey;
		this.memberId = memberId;
		this.idempotencyOperationType = idempotencyOperationType;
		this.idempotencyStatus = IdempotencyStatus.PENDING;
	}

	public static Idempotency createPending(
		String idempotencyKey,
		Long memberId,
		IdempotencyOperationType idempotencyOperationType
	) {
		return new Idempotency(
			idempotencyKey,
			memberId,
			idempotencyOperationType
		);
	}

	public void completeWithResource(Long resourceId) {
		this.idempotencyStatus = IdempotencyStatus.SUCCESS;
		this.resourceId = resourceId;
	}

	public boolean isCompleted() {
		return this.idempotencyStatus == IdempotencyStatus.SUCCESS;
	}
}
