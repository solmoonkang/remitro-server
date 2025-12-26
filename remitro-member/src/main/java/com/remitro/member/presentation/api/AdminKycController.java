package com.remitro.member.presentation.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.presentation.ApiSuccessResponse;
import com.remitro.common.security.AuthenticatedUser;
import com.remitro.common.security.CurrentUser;
import com.remitro.member.application.dto.response.PendingKycResponse;
import com.remitro.member.application.service.admin.AdminKycCommandService;
import com.remitro.member.application.service.admin.AdminKycQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/admin/members/kyc")
@RequiredArgsConstructor
@Tag(name = "관리자 KYC 심사 APIs", description = "관리자가 회원의 KYC 인증 요청을 승인 또는 거절하는 API")
public class AdminKycController {

	private final AdminKycCommandService adminKycCommandService;
	private final AdminKycQueryService adminKycQueryService;

	@PostMapping("/{memberId}/approve")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "KYC 인증 승인",
		description = "관리자가 회원의 KYC 인증 요청을 승인합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "🎉 KYC 인증 승인 성공",
			content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 KYC 요청"),
		@ApiResponse(responseCode = "403", description = "❗️ 관리자 권한 없음"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ApiSuccessResponse approveKyc(
		@PathVariable Long memberId,
		@CurrentUser AuthenticatedUser authenticatedUser
	) {
		adminKycCommandService.approveKycByAdmin(memberId, authenticatedUser.memberId());
		return ApiSuccessResponse.success("KYC 인증이 성공적으로 승인되었습니다.");
	}

	@PostMapping("/{memberId}/reject")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "KYC 인증 거절",
		description = "관리자가 회원의 KYC 인증 요청을 거절합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "🎉 KYC 인증 거절 성공",
			content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 거절 사유"),
		@ApiResponse(responseCode = "403", description = "❗️ 관리자 권한 없음"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ApiSuccessResponse rejectKyc(
		@PathVariable Long memberId,
		@CurrentUser AuthenticatedUser authenticatedUser,
		@RequestParam String reason
	) {
		adminKycCommandService.rejectKycByAdmin(memberId, authenticatedUser.memberId(), reason);
		return ApiSuccessResponse.success("KYC 인증이 성공적으로 거절되었습니다.");
	}

	@GetMapping("/pending")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "KYC 심사 대기 목록 조회",
		description = "현재 심사 대기 중인 모든 KYC 요청을 조회합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "🎉 KYC 심사 대기 목록 조회 성공",
			content = @Content(schema = @Schema(implementation = PendingKycResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 거절 사유"),
		@ApiResponse(responseCode = "403", description = "❗️ 관리자 권한 없음"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public List<PendingKycResponse> getPendingKycList() {
		return adminKycQueryService.findAllPendingKyc();
	}

	@GetMapping("/pending/{memberId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "특정 회원 KYC 심사 대기 조회",
		description = "특정 회원의 심사 대기 중인 KYC 요청을 조회합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "🎉 KYC 심사 대기 목록 조회 성공",
			content = @Content(schema = @Schema(implementation = PendingKycResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 거절 사유"),
		@ApiResponse(responseCode = "403", description = "❗️ 관리자 권한 없음"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public PendingKycResponse getPendingKyc(@PathVariable Long memberId) {
		return adminKycQueryService.findPendingKycByMember(memberId);
	}
}
