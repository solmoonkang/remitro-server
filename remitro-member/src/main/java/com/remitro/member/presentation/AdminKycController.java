package com.remitro.member.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.presentation.ApiSuccessResponse;
import com.remitro.member.application.command.KycResultCommandService;
import com.remitro.member.presentation.dto.request.KycRejectRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
@Tag(name = "관리자 KYC APIs", description = "관리자 KYC 승인/거절 관리 API")
public class AdminKycController {

	private final KycResultCommandService kycResultCommandService;

	@PostMapping("/{memberId}/kyc/approve")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "KYC 승인")
	public ApiSuccessResponse approveKyc(
		@PathVariable Long memberId
	) {
		kycResultCommandService.approve(memberId);
		return ApiSuccessResponse.success("KYC가 성공적으로 승인되었습니다.");
	}

	@PostMapping("/{memberId}/kyc/reject")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "KYC 거절")
	public ApiSuccessResponse rejectKyc(
		@PathVariable Long memberId,
		@Valid @RequestBody KycRejectRequest kycRejectRequest
	) {
		kycResultCommandService.reject(memberId, kycRejectRequest);
		return ApiSuccessResponse.success("KYC가 성공적으로 거절되었습니다.");
	}
}
