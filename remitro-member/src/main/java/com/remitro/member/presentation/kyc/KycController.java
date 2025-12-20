package com.remitro.member.presentation.kyc;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.security.AuthenticatedUser;
import com.remitro.common.security.CurrentUser;
import com.remitro.member.application.service.kyc.KycService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members/kyc")
@RequiredArgsConstructor
@Tag(name = "KYC 인증 APIs", description = "사용자 KYC 인증 요청 API")
public class KycController {

	private final KycService kycService;

	@PostMapping("/request")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "KYC 인증 요청",
		description = "해당 회원에 대해 KYC 인증을 요청 상태(PENDING)로 생성합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "🎉 KYC 요청 생성 성공"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<?> requestKyc(@CurrentUser AuthenticatedUser authenticatedUser) {
		kycService.requestKyc(authenticatedUser.memberId());
		return ResponseEntity.status(HttpStatus.CREATED).body("[✅ SUCCESS] KYC 요청을 성공적으로 생성했습니다.");
	}
}
