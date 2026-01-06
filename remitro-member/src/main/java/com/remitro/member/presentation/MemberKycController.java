package com.remitro.member.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.auth.AuthenticatedUser;
import com.remitro.common.presentation.ApiSuccessResponse;
import com.remitro.common.security.CurrentUser;
import com.remitro.member.application.command.KycRequestCommandService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "회원 KYC APIs", description = "회원 본인 인증(KYC) 요청 관련 API")
public class MemberKycController {

	private final KycRequestCommandService kycRequestCommandService;

	@PostMapping("/me/kyc/request")
	@ResponseStatus(HttpStatus.ACCEPTED)
	@Operation(
		summary = "KYC 인증 요청",
		description = "본인 인증을 시작합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "202",
			description = "KYC 요청 접수",
			content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "❌ 요청 불가 상태"),
		@ApiResponse(responseCode = "401", description = "❌ 인증 실패"),
		@ApiResponse(responseCode = "500", description = "💥 서버 오류")
	})
	public ApiSuccessResponse requestKyc(
		@CurrentUser AuthenticatedUser authenticatedUser
	) {
		kycRequestCommandService.requestKyc(authenticatedUser.memberId());
		return ApiSuccessResponse.success("KYC 인증 요청이 성공적으로 접수되었습니다.");
	}
}
