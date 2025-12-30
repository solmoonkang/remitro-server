package com.remitro.member.presentation.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.presentation.ApiSuccessResponse;
import com.remitro.common.security.AuthenticatedUser;
import com.remitro.common.security.CurrentUser;
import com.remitro.common.security.Role;
import com.remitro.member.application.usecase.admin.service.AdminMemberCommandService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/admin/members")
@RequiredArgsConstructor
@Tag(name = "관리자 회원 관리 APIs", description = "관리자가 회원의 권한 변경, 잠금 및 잠금 해제를 수행하는 API")
public class AdminMemberController {

	private final AdminMemberCommandService adminMemberCommandService;

	@PatchMapping("/{memberId}/role")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "회원 권한 변경",
		description = "관리자가 특정 회원의 권한을 변경합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "🎉 회원 권한 변경 성공",
			content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 권한 요청"),
		@ApiResponse(responseCode = "403", description = "❗️ 관리자 권한 없음"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ApiSuccessResponse changeMemberRole(
		@PathVariable Long memberId,
		@RequestParam Role nextRole,
		@CurrentUser AuthenticatedUser authenticatedUser
	) {
		adminMemberCommandService.changeMemberRole(memberId, nextRole, authenticatedUser.memberId());
		return ApiSuccessResponse.success("회원 권한이 성공적으로 변경되었습니다.");
	}

	@PostMapping("/{memberId}/lock")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "회원 강제 잠금",
		description = "관리자가 특정 회원을 강제로 잠금 처리합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "🎉 회원 잠금 처리 성공",
			content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 잠금 상태"),
		@ApiResponse(responseCode = "403", description = "❗️ 관리자 권한 없음"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ApiSuccessResponse lockMember(
		@PathVariable Long memberId,
		@CurrentUser AuthenticatedUser authenticatedUser
	) {
		adminMemberCommandService.lockMemberByAdmin(memberId, authenticatedUser.memberId());
		return ApiSuccessResponse.success("회원이 성공적으로 잠금 처리되었습니다.");
	}

	@PostMapping("/{memberId}/unlock")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "회원 잠금 해제",
		description = "관리자가 잠금 상태인 회원의 잠금을 해제합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "🎉 회원 잠금 해제 성공",
			content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 잠금 상태"),
		@ApiResponse(responseCode = "403", description = "❗️ 관리자 권한 없음"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ApiSuccessResponse unlockMember(
		@PathVariable Long memberId,
		@CurrentUser AuthenticatedUser authenticatedUser
	) {
		adminMemberCommandService.unlockMemberByAdmin(memberId, authenticatedUser.memberId());
		return ApiSuccessResponse.success("회원이 성공적으로 잠금 해제되었습니다.");
	}
}
