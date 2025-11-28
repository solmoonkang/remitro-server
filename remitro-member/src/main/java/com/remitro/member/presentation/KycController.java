package com.remitro.member.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.auth.LoginMemberId;
import com.remitro.member.application.dto.request.UpdateKycStatusRequest;
import com.remitro.member.application.service.kyc.KycService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members/kyc")
@RequiredArgsConstructor
@Tag(name = "KYC 인증 APIs", description = "KYC 인증 요청 및 심사 결과 처리 API")
public class KycController {

	private final KycService kycService;

	@PostMapping("/request")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "KYC 인증 요청", description = "해당 회원에 대해 KYC 인증을 요청 상태(PENDING)로 생성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "🎉 KYC 요청 생성 성공"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<?> requestKyc(@LoginMemberId Long memberId) {
		kycService.requestKyc(memberId);
		return ResponseEntity.status(HttpStatus.CREATED).body("[✅ SUCCESS] KYC 요청을 성공적으로 생성했습니다.");
	}

	@PatchMapping("/complete")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "KYC 인증 심사 결과 처리",
		description = "KYC 인증 심사 결과(VERIFIED/REJECTED/PENDING)를 반영합니다. VERIFIED가 아닐 경우 사유는 필수입니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 KYC 요청 생성 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 요청 데이터"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<?> completeKyc(
		@LoginMemberId Long memberId,
		@Valid @RequestBody UpdateKycStatusRequest updateKycStatusRequest
	) {
		kycService.completeKyc(memberId, updateKycStatusRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] KYC 심사 결과가 성공적으로 반영되었습니다.");
	}
}
