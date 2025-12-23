package com.remitro.member.application.service.admin;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.ForbiddenException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.member.application.service.member.MemberEventPublisher;
import com.remitro.member.application.service.member.MemberReadService;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

	private final MemberReadService memberReadService;
	private final MemberEventPublisher memberEventPublisher;
	private final Clock clock;

	@Transactional
	public void lockMemberByAdmin(Long memberId, Long adminMemberId) {
		final Member adminMember = memberReadService.findMemberById(adminMemberId);
		validateAdminPermission(adminMember);

		final Member targetMember = memberReadService.findMemberById(memberId);
		targetMember.lockByAdmin();
		memberEventPublisher.publishMemberLocked(targetMember, adminMemberId);
	}

	@Transactional
	public void unlockMemberByAdmin(Long memberId, Long adminMemberId) {
		final Member adminMember = memberReadService.findMemberById(adminMemberId);
		validateAdminPermission(adminMember);

		final Member targetMember = memberReadService.findMemberById(memberId);
		targetMember.unlockByAdmin(LocalDateTime.now(clock));
		memberEventPublisher.publishMemberUnlockedByAdmin(targetMember, adminMemberId, LocalDateTime.now(clock));
	}

	private void validateAdminPermission(Member adminMember) {
		if (!adminMember.getRole().isAdmin()) {
			throw new ForbiddenException(
				ErrorCode.ADMIN_PERMISSION_REQUIRED,
				ErrorMessage.ADMIN_PERMISSION_REQUIRED
			);
		}
	}
}
