package com.remitro.member.application.service.admin;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.ForbiddenException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.common.security.Role;
import com.remitro.event.member.enums.LockActorType;
import com.remitro.event.member.enums.UnlockActorType;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.enums.LockReason;
import com.remitro.member.domain.model.Member;
import com.remitro.member.infrastructure.messaging.MemberEventPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminMemberCommandService {

	private final MemberFinder memberFinder;
	private final MemberEventPublisher memberEventPublisher;
	private final Clock clock;

	@Transactional
	public void lockMemberByAdmin(Long memberId, Long adminMemberId) {
		final Member adminMember = memberFinder.getById(adminMemberId);
		validateAdminPermission(adminMember);

		final Member targetMember = memberFinder.getById(memberId);
		targetMember.lockByAdmin();

		memberEventPublisher.publishMemberLocked(
			targetMember,
			adminMemberId,
			LockActorType.ADMIN,
			LockReason.ADMIN_ACTION,
			LocalDateTime.now(clock)
		);
	}

	@Transactional
	public void unlockMemberByAdmin(Long memberId, Long adminMemberId) {
		final Member adminMember = memberFinder.getById(adminMemberId);
		validateAdminPermission(adminMember);

		final Member targetMember = memberFinder.getById(memberId);
		targetMember.unlockByAdmin(LocalDateTime.now(clock));

		memberEventPublisher.publishMemberUnlocked(
			targetMember,
			adminMemberId,
			UnlockActorType.ADMIN,
			LocalDateTime.now(clock)
		);
	}

	@Transactional
	public void changeMemberRole(Long targetMemberId, Role nextRole, Long adminMemberId) {
		final Member adminMember = memberFinder.getById(adminMemberId);

		if (!adminMember.getRole().isAdmin()) {
			throw new ForbiddenException(
				ErrorCode.ADMIN_PERMISSION_REQUIRED, ErrorMessage.ADMIN_PERMISSION_REQUIRED
			);
		}

		final Member targetMember = memberFinder.getById(targetMemberId);
		final Role previousRole = targetMember.getRole();
		targetMember.changeRole(nextRole);

		memberEventPublisher.publishMemberRoleChanged(
			targetMember,
			previousRole,
			adminMemberId,
			LocalDateTime.now(clock)
		);
	}

	private void validateAdminPermission(Member adminMember) {
		if (!adminMember.getRole().isAdmin()) {
			throw new ForbiddenException(
				ErrorCode.ADMIN_PERMISSION_REQUIRED, ErrorMessage.ADMIN_PERMISSION_REQUIRED
			);
		}
	}
}
