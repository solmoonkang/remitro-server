package com.remitro.member.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.response.CommonResponse;
import com.remitro.common.security.AuthenticatedUser;
import com.remitro.common.security.CurrentUser;
import com.remitro.member.application.command.AdminMemberCommandService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/members")
@RequiredArgsConstructor
@Tag(name = "ADMIN", description = "관리자 전용 회원 관리 API")
public class AdminMemberController {

	private final AdminMemberCommandService adminMemberCommandService;

	@Operation(
		summary = "회원 계정 정지",
		description = "신고 또는 비정상 행위가 감지된 회원을 수동으로 정지 처리합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "정지 처리 성공"),
		@ApiResponse(responseCode = "403", description = "관리자 권한 없음"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
	})
	@PatchMapping("/{memberId}/suspend")
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<Void> suspend(
		@CurrentUser AuthenticatedUser authenticatedUser,
		@PathVariable Long memberId
	) {
		adminMemberCommandService.suspend(authenticatedUser.memberId(), memberId);
		return CommonResponse.successNoContent();
	}

	@Operation(
		summary = "회원 계정 정지 해제",
		description = "정지 상태인 회원을 정상 상태로 복구합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "해제 처리 성공"),
		@ApiResponse(responseCode = "403", description = "관리자 권한 없음"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
	})
	@PatchMapping("/{memberId}/unsuspend")
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<Void> unsuspend(
		@CurrentUser AuthenticatedUser authenticatedUser,
		@PathVariable Long memberId
	) {
		adminMemberCommandService.unsuspend(authenticatedUser.memberId(), memberId);
		return CommonResponse.successNoContent();
	}
}
