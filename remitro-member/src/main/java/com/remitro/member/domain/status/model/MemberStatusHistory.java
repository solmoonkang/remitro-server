package com.remitro.member.domain.status.model;

import java.time.LocalDateTime;

import com.remitro.member.domain.member.enums.ActivityStatus;
import com.remitro.member.domain.status.enums.StatusChangeReason;

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
@Table(name = "MEMBER_STATUS_HISTORIES", indexes = {
	@Index(name = "idx_member_status_histories_member_id", columnList = "member_id"),
	@Index(name = "idx_member_status_histories_occurred_at", columnList = "occurred_at"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberStatusHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_status_history_id")
	private Long id;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Enumerated(EnumType.STRING)
	@Column(name = "previous_status", nullable = false, length = 20)
	private ActivityStatus previousStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "new_status", nullable = false, length = 20)
	private ActivityStatus newStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "status_change_reason", nullable = false, length = 30)
	private StatusChangeReason statusChangeReason;

	@Column(name = "occurred_at", nullable = false)
	private LocalDateTime occurredAt;

	private MemberStatusHistory(
		Long memberId,
		ActivityStatus previousStatus,
		ActivityStatus newStatus,
		StatusChangeReason statusChangeReason,
		LocalDateTime occurredAt
	) {
		this.memberId = memberId;
		this.previousStatus = previousStatus;
		this.newStatus = newStatus;
		this.statusChangeReason = statusChangeReason;
		this.occurredAt = occurredAt;
	}

	public static MemberStatusHistory record(
		Long memberId,
		ActivityStatus previousStatus,
		ActivityStatus newStatus,
		StatusChangeReason statusChangeReason,
		LocalDateTime occurredAt
	) {
		return new MemberStatusHistory(
			memberId,
			previousStatus,
			newStatus,
			statusChangeReason,
			occurredAt
		);
	}
}
