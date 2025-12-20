package com.remitro.member.presentation.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.security.AuthenticatedUser;
import com.remitro.common.security.CurrentUser;
import com.remitro.common.security.Role;
import com.remitro.member.application.service.member.MemberRoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/admin/members")
@RequiredArgsConstructor
@Tag(name = "관리자 회원 관리 APIs", description = "관리자 전용 회원 관리 API")
public class AdminMemberController {

	private final MemberRoleService memberRoleService;

	@PatchMapping("/{memberId}/role")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "회원 권한 변경",
		description = "관리자가 특정 회원의 권한을 변경합니다."
	)
	public ResponseEntity<?> updateMemberRole(
		@PathVariable Long memberId,
		@RequestParam Role nextRole,
		@CurrentUser AuthenticatedUser authenticatedUser
	) {
		memberRoleService.updateMemberRole(memberId, nextRole, authenticatedUser.memberId());
		return ResponseEntity.ok("[✅ SUCCESS] 회원 권한이 성공적으로 변경되었습니다.");
	}
}
