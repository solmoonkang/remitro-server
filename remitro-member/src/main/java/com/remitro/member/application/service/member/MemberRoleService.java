package com.remitro.member.application.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.error.exception.ForbiddenException;
import com.remitro.common.error.model.ErrorMessage;
import com.remitro.common.security.Role;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberRoleService {

	private final MemberReadService memberReadService;
	private final MemberWriteService memberWriteService;

	@Transactional
	public void updateMemberRole(Long targetMemberId, Role nextRole, Long adminMemberId) {
		final Member adminMember = memberReadService.findMemberById(adminMemberId);

		if (!adminMember.getRole().isAdmin()) {
			throw new ForbiddenException(ErrorMessage.ADMIN_PERMISSION_REQUIRED);
		}

		final Member targetMember = memberReadService.findMemberById(targetMemberId);

		memberWriteService.updateRole(targetMember, nextRole, adminMemberId);
	}
}
