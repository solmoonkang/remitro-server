package com.remitro.account.domain.projection.model;

import com.remitro.account.domain.projection.enums.MemberActivityStatus;

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
	@Column(name = "member_id", unique = true, nullable = false)
	private Long memberId;

	@Enumerated(EnumType.STRING)
	@Column(name = "activity_status", nullable = false, length = 20)
	private MemberActivityStatus activityStatus;

	public boolean isActive() {
		return activityStatus == MemberActivityStatus.ACTIVE;
	}
}
