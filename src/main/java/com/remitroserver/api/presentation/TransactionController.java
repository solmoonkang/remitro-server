package com.remitroserver.api.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitroserver.api.application.transaction.TransactionService;
import com.remitroserver.api.domain.auth.model.AuthMember;
import com.remitroserver.api.dto.transaction.request.TransferRequest;
import com.remitroserver.global.auth.annotation.Auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
@Tag(name = "송금 APIs", description = "계좌 간 송금 처리 및 거래 내역 관리 API")
public class TransactionController {

	private final TransactionService transactionService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "송금 요청 - 계좌 간 자금 이체",
		description = "사용자가 보유한 계좌에서 다른 계좌로 송금을 수행합니다. 송금 금액과 계좌 상태, 중복 여부 등 다양한 검증이 포함됩니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "🎉 송금 요청 완료"),
		@ApiResponse(responseCode = "400", description = "❌ 잘못된 송금 요청 (잔액 부족, 동일 계좌 송금, 계좌 비활성 등)"),
		@ApiResponse(responseCode = "401", description = "🚫 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자 또는 계좌"),
		@ApiResponse(responseCode = "409", description = "⚠️ 이미 처리된 송금 요청 (멱등성 키 중복)"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<String> transferFunds(
		@Auth AuthMember authMember,
		@RequestBody @Valid TransferRequest transferRequest) {

		transactionService.transferFunds(authMember, transferRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 송금 요청이 성공적으로 처리되었습니다.");
	}
}
