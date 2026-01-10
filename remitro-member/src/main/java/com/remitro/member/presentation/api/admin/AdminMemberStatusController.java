package com.remitro.member.presentation.api.admin;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.presentation.ApiSuccessResponse;
import com.remitro.member.application.command.MemberUnlockCommandService;
import com.remitro.member.application.command.MemberWithdrawCommandService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
@Tag(name = "관리자 회원 상태 APIs", description = "회원 잠금/해제/탈퇴 관리 API")
public class AdminMemberStatusController {

	private final MemberUnlockCommandService memberUnlockCommandService;
	private final MemberWithdrawCommandService memberWithdrawCommandService;

	@PostMapping("/{memberId}/unlock")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "회원 잠금")
	public ApiSuccessResponse unlock(@PathVariable Long memberId) {
		memberUnlockCommandService.unlock(memberId);
		return ApiSuccessResponse.success("회원 잠금이 성공적으로 해제되었습니다.");
	}

	@PostMapping("/{memberId}/withdraw")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "회원 탈퇴")
	public ApiSuccessResponse withdraw(@PathVariable Long memberId) {
		memberWithdrawCommandService.withdraw(memberId);
		return ApiSuccessResponse.success("회원 탈퇴가 성공적으로 처리되었습니다.");
	}
}
