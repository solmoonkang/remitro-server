package com.remitro.transaction.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.auth.annotation.Auth;
import com.remitro.common.auth.model.AuthMember;
import com.remitro.transaction.application.dto.response.TransactionDetailResponse;
import com.remitro.transaction.application.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "거래내역 APIs", description = "거래내역 정보 송금, 입출금 관련 API")
public class TransactionController {

	private final TransactionService transactionService;

	@GetMapping("/{transactionId}/detail")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "특정 거래내역 조회", description = "사용자 인증 후 특정 거래의 상세 정보를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 특정 거래내역 조회 성공"),
		@ApiResponse(responseCode = "403", description = "❗️ 거래내역 접근 권한 없음"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 거래내역"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<TransactionDetailResponse> findTransactionDetail(
		@Auth AuthMember authMember,
		@PathVariable Long transactionId) {

		return ResponseEntity.ok().body(transactionService.findTransactionDetail(authMember, transactionId));
	}

	@GetMapping("/{accountId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "계좌 전체 거래내역 조회", description = "사용자 인증 후 특정 거래의 모든 거래내역을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 모든 거래내역 조회 성공"),
		@ApiResponse(responseCode = "403", description = "❗️ 거래내역 접근 권한 없음"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 거래내역"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<List<TransactionDetailResponse>> findAllTransactions(
		@Auth AuthMember authMember,
		@PathVariable Long accountId) {

		return ResponseEntity.ok().body(transactionService.findAllTransactions(authMember, accountId));
	}
}
