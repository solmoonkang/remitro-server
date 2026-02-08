package com.remitro.account.domain.projection.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;

import com.remitro.account.domain.projection.enums.MemberProjectionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "MEMBER_PROJECTIONS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProjection {

	@Id
	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Comment("회원 닉네임")
	@Column(name = "nickname", nullable = false)
	private String nickname;

	@Comment("회원 상태")
	@Enumerated(EnumType.STRING)
	@Column(name = "member_status", nullable = false)
	private MemberProjectionStatus memberStatus;

	@Comment("마지막 동기화 일시")
	@Column(name = "last_updated_at")
	private LocalDateTime lastUpdatedAt;

	private MemberProjection(Long memberId, String nickname, MemberProjectionStatus memberStatus) {
		this.memberId = memberId;
		this.nickname = nickname;
		this.memberStatus = memberStatus;
	}

	public static MemberProjection create(Long memberId, String nickname, MemberProjectionStatus memberStatus) {
		return new MemberProjection(memberId, nickname, memberStatus);
	}

	public boolean isActive() {
		return this.memberStatus == MemberProjectionStatus.ACTIVE;
	}
}
