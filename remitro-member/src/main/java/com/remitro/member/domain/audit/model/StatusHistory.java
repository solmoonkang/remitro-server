package com.remitro.member.domain.audit.model;

import org.hibernate.annotations.Comment;

import com.remitro.common.security.Role;
import com.remitro.member.domain.audit.enums.ChangeReason;
import com.remitro.member.domain.member.enums.LoginSecurityStatus;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.audit.enums.StatusType;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.common.BaseTimeEntity;

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
@Table(
	name = "MEMBER_STATUS_HISTORIES",
	indexes = {
		@Index(name = "idx_member_status_history_member_created_at", columnList = "member_id, created_at"),
		@Index(name = "idx_member_status_history_type_created_at", columnList = "status_type, created_at")
	}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StatusHistory extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_status_history_id")
	private Long id;

	@Comment("회원 ID")
	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Comment("")
	@Enumerated(EnumType.STRING)
	@Column(name = "status_type", nullable = false, length = 30)
	private StatusType statusType;

	@Comment("변경 전 회원 상태")
	@Column(name = "previous_status", nullable = false, length = 20)
	private String previousStatus;

	@Comment("변경 후 회원 상태")
	@Column(name = "current_status", nullable = false, length = 20)
	private String currentStatus;

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
		Long memberId,
		StatusType statusType,
		String previousStatus,
		String currentStatus,
		ChangeReason changeReason,
		Role changedByRole,
		Long changedById
	) {
		this.memberId = memberId;
		this.statusType = statusType;
		this.previousStatus = previousStatus;
		this.currentStatus = currentStatus;
		this.changeReason = changeReason;
		this.changedByRole = changedByRole;
		this.changedById = changedById;
	}

	// MemberStatus 시스템 자동 처리용
	public static StatusHistory ofSystem(Member member, MemberStatus previousStatus, ChangeReason changeReason) {
		return new StatusHistory(
			member.getId(),
			StatusType.MEMBER_STATUS,
			previousStatus.name(),
			member.getMemberStatus().name(),
			changeReason,
			Role.SYSTEM,
			null
		);
	}

	// MemberStatus 수동 변경용 (관리자/유저)
	public static StatusHistory ofManual(
		Member member,
		MemberStatus previousStatus,
		ChangeReason changeReason,
		Role changedByRole,
		Long changedById
	) {
		return new StatusHistory(
			member.getId(),
			StatusType.MEMBER_STATUS,
			previousStatus.name(),
			member.getMemberStatus().name(),
			changeReason,
			changedByRole,
			changedById
		);
	}

	// LoginSecurityStatus 시스템 자동 처리용
	public static StatusHistory ofSecuritySystem(
		Member member,
		LoginSecurityStatus previousStatus,
		ChangeReason changeReason
	) {
		return new StatusHistory(
			member.getId(),
			StatusType.LOGIN_SECURITY_STATUS,
			previousStatus.name(),
			member.getLoginSecurityStatus().name(),
			changeReason,
			Role.SYSTEM,
			null
		);
	}

	// 4. LoginSecurityStatus 수동 변경용 (관리자 등)
	public static StatusHistory ofSecurityManual(
		Member member,
		LoginSecurityStatus previousStatus,
		ChangeReason changeReason,
		Role changedByRole,
		Long changedById
	) {
		return new StatusHistory(
			member.getId(),
			StatusType.LOGIN_SECURITY_STATUS,
			previousStatus.name(),
			member.getLoginSecurityStatus().name(),
			changeReason,
			changedByRole,
			changedById
		);
	}
}
