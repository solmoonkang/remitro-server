package com.remitro.member.domain.audit.model;

import org.hibernate.annotations.Comment;

import com.remitro.member.domain.audit.enums.LoginStatus;
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
	name = "LOGIN_HISTORIES",
	indexes = {
		@Index(name = "idx_login_history_member_id", columnList = "member_id"),
		@Index(name = "idx_login_history_created_at", columnList = "created_at")
	}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginHistory extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "login_history_id")
	private Long id;

	@Comment("회원 ID")
	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Comment("접속 IP 주소")
	@Column(name = "client_ip", nullable = false, length = 45)
	private String clientIp;

	@Comment("접속 기기 정보")
	@Column(name = "user_agent", nullable = false, length = 500)
	private String userAgent;

	@Comment("로그인 성공 여부")
	@Enumerated(EnumType.STRING)
	@Column(name = "login_status", nullable = false, length = 20)
	private LoginStatus loginStatus;

	@Comment("로그인 실패 사유")
	@Column(name = "failure_reason")
	private String failureReason;

	private LoginHistory(
		Long memberId,
		String clientIp,
		String userAgent,
		LoginStatus loginStatus,
		String failureReason
	) {
		this.memberId = memberId;
		this.clientIp = clientIp;
		this.userAgent = userAgent;
		this.loginStatus = loginStatus;
		this.failureReason = failureReason;
	}

	public static LoginHistory ofSuccess(Long memberId, String clientIp, String userAgent) {
		return new LoginHistory(memberId, clientIp, userAgent, LoginStatus.SUCCESS, null);
	}

	public static LoginHistory ofFailure(Long memberId, String clientIp, String userAgent, String failureReason) {
		return new LoginHistory(memberId, clientIp, userAgent, LoginStatus.FAILURE, failureReason);
	}
}
