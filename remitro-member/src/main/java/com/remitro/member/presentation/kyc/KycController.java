package com.remitro.member.presentation.kyc;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.presentation.ApiSuccessResponse;
import com.remitro.common.security.AuthenticatedUser;
import com.remitro.common.security.CurrentUser;
import com.remitro.member.application.service.kyc.KycRequestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members/kyc")
@RequiredArgsConstructor
@Tag(name = "KYC 인증 APIs", description = "사용자 KYC 인증 요청 API")
public class KycController {

	private final KycRequestService kycRequestService;

	@PostMapping("/request")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "KYC 인증 요청",
		description = "사용자가 본인의 KYC 인증을 요청합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "201",
			description = "🎉 KYC 요청 생성 성공",
			content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "❌ KYC 요청 불가 상태"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ApiSuccessResponse requestKyc(@CurrentUser AuthenticatedUser authenticatedUser) {
		kycRequestService.requestKyc(authenticatedUser.memberId());
		return ApiSuccessResponse.success("KYC 요청이 성공적으로 생성되었습니다.");
	}
}
