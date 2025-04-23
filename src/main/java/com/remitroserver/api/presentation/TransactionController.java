package com.remitroserver.api.presentation;

import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitroserver.api.application.transaction.TransactionService;
import com.remitroserver.api.domain.auth.model.AuthMember;
import com.remitroserver.api.dto.transaction.request.TransactionSearchRequest;
import com.remitroserver.api.dto.transaction.request.TransferRequest;
import com.remitroserver.api.dto.transaction.response.TransactionDetailResponse;
import com.remitroserver.api.dto.transaction.response.TransactionSummaryResponse;
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
@Tag(name = "거래 APIs", description = "계좌 간 송금 처리 및 거래 내역 관리 API")
public class TransactionController {

	private final TransactionService transactionService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "송금 요청 - 계좌 간 자금 이체",
		description = "사용자가 송금을 요청합니다. 요청은 '대기(REQUESTED)' 상태로 저장되며, 이후 별도 승인 요청을 통해 송금이 완료됩니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "🎉 송금 요청 완료"),
		@ApiResponse(responseCode = "400", description = "❌ 잘못된 송금 요청 (잔액 부족, 동일 계좌 송금)"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자 또는 계좌"),
		@ApiResponse(responseCode = "409", description = "⚠️ 이미 처리된 송금 요청 (멱등성 키 중복)"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<String> requestTransfer(
		@Auth AuthMember authMember,
		@RequestBody @Valid TransferRequest transferRequest) {

		transactionService.requestTransfer(authMember, transferRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 송금 요청이 성공적으로 처리되었습니다.");
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "거래 내역 조회 - 사용자 계좌 기준 조건별 거래 내역 조회",
		description = "사용자의 특정 계좌 기준으로 송금 및 수신 거래 내역을 조회합니다. " +
			"거래일(fromDate, toDate) 및 상태(status) 조건으로 필터링할 수 있으며, 결과는 최신순으로 정렬되어 반환됩니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "✅ 거래 내역 조회 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 잘못된 요청 파라미터 (날짜 형식 오류 등)"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않거나 소유하지 않은 계좌"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<List<TransactionSummaryResponse>> findMyAllTransactionsByCondition(
		@RequestParam UUID accountToken,
		@Auth AuthMember authMember,
		@ParameterObject TransactionSearchRequest transactionSearchRequest) {

		return ResponseEntity.ok().body(transactionService.findMyAllTransactionsByCondition(
			accountToken, authMember, transactionSearchRequest)
		);
	}

	@GetMapping("/{transactionToken}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "거래 상세 조회 - 단일 거래 내역 확인",
		description = "사용자가 자신의 계좌로 발생한 특정 거래의 상세 정보를 확인합니다. " +
			"출금/입금 계좌 번호, 닉네임, 거래 금액, 상태 로그 등을 포함한 상세 정보를 제공합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "✅ 거래 상세 정보 조회 성공"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "403", description = "🚫 접근 권한 부족"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 거래 정보"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<TransactionDetailResponse> findTransactionDetail(
		@PathVariable UUID transactionToken,
		@Auth AuthMember authMember) {

		return ResponseEntity.ok().body(transactionService.findTransactionDetail(transactionToken, authMember));
	}

	@PatchMapping("/{transactionToken}/approve")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "송금 승인 - 요청된 거래 승인 처리",
		description = "송금 요청 후, 5분 이내에 사용자가 거래를 승인하면 자금 이체가 완료됩니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "✅ 송금 승인 완료"),
		@ApiResponse(responseCode = "400", description = "❌ 거래 상태 오류 또는 만료"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "404", description = "🔍 거래 정보를 찾을 수 없음"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<String> approveTransfer(@PathVariable UUID transactionToken, @Auth AuthMember authMember) {
		transactionService.approveTransfer(transactionToken, authMember);
		return ResponseEntity.ok().body("[✅ SUCCESS] 송금 요청이 성공적으로 승인되었습니다.");
	}

	@PatchMapping("/{transactionToken}/cancel")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "송금 취소 - 요청된 거래 취소 처리",
		description = "승인 대기 상태인 송금 요청을 사용자가 취소합니다. 승인된 거래는 취소할 수 없습니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "✅ 송금 취소 완료"),
		@ApiResponse(responseCode = "400", description = "❌ 잘못된 상태의 거래 취소 요청"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "403", description = "🚫 접근 권한 부족"),
		@ApiResponse(responseCode = "404", description = "🔍 거래 정보를 찾을 수 없음"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<String> cancelTransfer(@PathVariable UUID transactionToken, @Auth AuthMember authMember) {
		transactionService.cancelTransfer(transactionToken, authMember);
		return ResponseEntity.ok().body("[✅ SUCCESS] 송금 요청이 성공적으로 취소되었습니다.");
	}
}
