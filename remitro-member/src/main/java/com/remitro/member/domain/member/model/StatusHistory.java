package com.remitro.member.domain.member.model;

import org.hibernate.annotations.Comment;

import com.remitro.common.security.Role;
import com.remitro.member.domain.member.enums.ChangeReason;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.infrastructure.persistence.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
	name = "MEMBER_STATUS_HISTORIES",
	indexes = {
		@Index(name = "idx_member_status_history_member_created_at", columnList = "member_id, created_at")
	}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StatusHistory extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_status_history_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Comment("변경 전 회원 상태")
	@Enumerated(EnumType.STRING)
	@Column(name = "previous_status", nullable = false, length = 20)
	private MemberStatus previousStatus;

	@Comment("변경 후 회원 상태")
	@Enumerated(EnumType.STRING)
	@Column(name = "current_status", nullable = false, length = 20)
	private MemberStatus currentStatus;

	@Comment("상태 변경 사유")
	@Enumerated(EnumType.STRING)
	@Column(name = "change_reason", nullable = false, length = 20)
	private ChangeReason changeReason;

	@Comment("상태 변경 주체 역할")
	@Column(name = "changed_by_role", nullable = false, length = 10)
	private Role changedByRole;

	@Comment("상태 변경 주체 ID")
	@Column(name = "changed_by_id")
	private Long changedById;

	private StatusHistory(
		Member member,
		MemberStatus previousStatus,
		MemberStatus currentStatus,
		ChangeReason changeReason,
		Role changedByRole,
		Long changedById
	) {
		this.member = member;
		this.previousStatus = previousStatus;
		this.currentStatus = currentStatus;
		this.changeReason = changeReason;
		this.changedByRole = changedByRole;
		this.changedById = changedById;
	}

	public static StatusHistory record(
		Member member,
		MemberStatus previousStatus,
		MemberStatus currentStatus,
		ChangeReason changeReason,
		Role changedByRole
	) {
		return new StatusHistory(
			member,
			previousStatus,
			currentStatus,
			changeReason,
			changedByRole,
			member.getId()
		);
	}
}
