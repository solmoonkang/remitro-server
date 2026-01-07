package com.remitro.member.domain.status.model;

import java.time.LocalDateTime;

import com.remitro.member.domain.member.model.ActivityStatus;
import com.remitro.member.domain.member.model.LockReason;
import com.remitro.member.domain.member.model.Member;

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
	@Column(name = "from_status", nullable = false, length = 20)
	private ActivityStatus fromStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "to_status", nullable = false, length = 20)
	private ActivityStatus toStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "lock_reason", nullable = false, length = 30)
	private LockReason lockReason;

	@Column(name = "occurred_at", nullable = false)
	private LocalDateTime occurredAt;

	private MemberStatusHistory(
		Long memberId,
		ActivityStatus fromStatus,
		ActivityStatus toStatus,
		LockReason lockReason,
		LocalDateTime occurredAt
	) {
		this.memberId = memberId;
		this.fromStatus = fromStatus;
		this.toStatus = toStatus;
		this.lockReason = lockReason;
		this.occurredAt = occurredAt;
	}

	public static MemberStatusHistory record(
		Member member,
		ActivityStatus fromStatus,
		LockReason lockReason,
		LocalDateTime occurredAt
	) {
		return new MemberStatusHistory(
			member.getId(),
			fromStatus,
			member.getActivityStatus(),
			lockReason,
			occurredAt
		);
	}
}
