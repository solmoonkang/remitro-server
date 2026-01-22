package com.remitro.member.domain.member.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.BadRequestException;
import com.remitro.common.security.Role;
import com.remitro.member.domain.member.enums.LoginSecurityStatus;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.infrastructure.persistence.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
	name = "MEMBER",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_member_email", columnNames = "email"),
		@UniqueConstraint(name = "uk_member_nickname", columnNames = "nickname"),
		@UniqueConstraint(name = "uk_member_phone", columnNames = "phone_number"),
	},
	indexes = {
		@Index(name = "idx_member_status_last_login", columnList = "member_status, last_login_at"),
		@Index(name = "idx_member_status_suspend_until", columnList = "member_status, suspend_until")
	}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Comment("이메일")
	@Column(name = "email", nullable = false, length = 100)
	private String email;

	@Comment("해시 비밀번호")
	@Column(name = "password", nullable = false)
	private String password;

	@Comment("닉네임")
	@Column(name = "nickname", nullable = false, length = 50)
	private String nickname;

	@Comment("전화번호")
	@Column(name = "phone_number", nullable = false, length = 20)
	private String phoneNumber;

	@Comment("회원 상태")
	@Enumerated(EnumType.STRING)
	@Column(name = "member_status", nullable = false, length = 20)
	private MemberStatus memberStatus;

	@Comment("로그인 보안 상태")
	@Enumerated(EnumType.STRING)
	@Column(name = "login_security_status", nullable = false, length = 20)
	private LoginSecurityStatus loginSecurityStatus;

	@Comment("회원 권한")
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, length = 20)
	private Role role;

	@Comment("로그인 실패 횟수")
	@Column(name = "failed_count", nullable = false, columnDefinition = "int default 0")
	private int failedCount;

	@Comment("최근 로그인 일시")
	@Column(name = "last_login_at")
	private LocalDateTime lastLoginAt;

	@Comment("계정 잠금 일시")
	@Column(name = "locked_at")
	private LocalDateTime lockedAt;

	@Comment("계정 휴면 일시")
	@Column(name = "dormant_at")
	private LocalDateTime dormantAt;

	@Comment("계정 정지 일시")
	@Column(name = "suspended_at")
	private LocalDateTime suspendedAt;

	@Comment("정지 해제 예정 시각")
	@Column(name = "suspend_until")
	private LocalDateTime suspendUntil;

	@Version
	@Column(name = "version")
	private Long version;

	private Member(String email, String password, String nickname, String phoneNumber) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
		this.memberStatus = MemberStatus.ACTIVE;
		this.loginSecurityStatus = LoginSecurityStatus.NORMAL;
		this.role = Role.USER;
		this.failedCount = 0;
	}

	public static Member register(String email, String password, String nickname, String phoneNumber) {
		return new Member(email, password, nickname, phoneNumber);
	}

	public void updateProfile(String newNickname, String newPhoneNumber) {
		this.nickname = newNickname;
		this.phoneNumber = newPhoneNumber;
	}

	public void changePassword(String newPassword) {
		this.password = newPassword;
	}

	public void increaseFailedCount(LocalDateTime now) {
		this.failedCount++;
		if (this.failedCount >= 5) {
			this.loginSecurityStatus = LoginSecurityStatus.LOCKED;
			this.lockedAt = now;
		}
	}

	public void resetFailedCount(LocalDateTime now) {
		this.loginSecurityStatus = LoginSecurityStatus.NORMAL;
		this.failedCount = 0;
		this.lastLoginAt = now;
		this.lockedAt = null;
	}

	public boolean isUnlockable(LocalDateTime now) {
		return this.loginSecurityStatus == LoginSecurityStatus.LOCKED
			&& this.lockedAt != null
			&& this.lockedAt.plusMinutes(10).isBefore(now);
	}

	public void unlock() {
		this.loginSecurityStatus = LoginSecurityStatus.NORMAL;
		this.failedCount = 0;
		this.lockedAt = null;
	}

	public boolean isDormant() {
		return this.memberStatus == MemberStatus.DORMANT;
	}

	public void changeToDormant(LocalDateTime now) {
		this.memberStatus = MemberStatus.DORMANT;
		this.dormantAt = now;
	}

	public void activate(LocalDateTime now) {
		this.memberStatus = MemberStatus.ACTIVE;
		this.lastLoginAt = now;
		this.dormantAt = null;
	}

	public void suspend(LocalDateTime now, LocalDateTime until) {
		// 정지는 운영 상태만 변경하며, 보안 상태(LoginSecurityStatus)는 변경하지 않는다.
		if (this.memberStatus == MemberStatus.SUSPENDED) {
			throw new BadRequestException(ErrorCode.ALREADY_SUSPENDED);
		}
		if (until != null && until.isBefore(now)) {
			throw new BadRequestException(ErrorCode.INVALID_SUSPEND_UNTIL);
		}
		this.memberStatus = MemberStatus.SUSPENDED;
		this.suspendedAt = now;
		this.suspendUntil = until;
	}

	public void unsuspend() {
		if (this.memberStatus != MemberStatus.SUSPENDED) {
			throw new BadRequestException(ErrorCode.NOT_SUSPENDED);
		}
		this.memberStatus = MemberStatus.ACTIVE;
		this.suspendedAt = null;
		this.suspendUntil = null;
	}

	public void suspendBySystem(LocalDateTime now, LocalDateTime until) {
		if (this.memberStatus == MemberStatus.SUSPENDED) {
			return;
		}
		if (until != null && until.isBefore(now)) {
			throw new BadRequestException(ErrorCode.INVALID_SUSPEND_UNTIL);
		}
		this.memberStatus = MemberStatus.SUSPENDED;
		this.suspendedAt = now;
		this.suspendUntil = until;
	}
}
