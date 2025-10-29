package com.remitro.account.application.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.account.application.dto.request.AccountPasswordRequest;
import com.remitro.account.application.dto.request.CreateAccountRequest;
import com.remitro.account.application.dto.request.DepositFormRequest;
import com.remitro.account.application.dto.request.TransferFormRequest;
import com.remitro.account.application.dto.request.UpdateStatusRequest;
import com.remitro.account.application.dto.request.WithdrawFormRequest;
import com.remitro.account.application.dto.response.AccountDetailResponse;
import com.remitro.account.domain.service.transaction.DepositTransactionHandler;
import com.remitro.account.domain.service.AccountService;
import com.remitro.account.domain.service.transaction.TransferTransactionHandler;
import com.remitro.account.domain.service.transaction.WithdrawTransactionHandler;
import com.remitro.common.auth.annotation.Auth;
import com.remitro.common.auth.model.AuthMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "계좌 APIs", description = "계좌 정보 저장, 송금, 입출금, 조회 관련 API")
public class AccountController {

	private final AccountService accountService;
	private final DepositTransactionHandler depositTransactionHandler;
	private final WithdrawTransactionHandler withdrawTransactionHandler;
	private final TransferTransactionHandler transferTransactionHandler;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "계좌 생성", description = "사용자 인증 후 계좌 정보를 생성했습니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "🎉 계좌 생성 성공"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<?> createAccount(
		@Auth AuthMember authMember,
		@Valid @RequestBody CreateAccountRequest createAccountRequest) {

		accountService.createAccount(authMember, createAccountRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 계좌 생성이 성공적으로 완료되었습니다.");
	}

	@PostMapping("/{accountId}/detail")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "특정 계좌 정보 조회", description = "사용자 인증 후 특정 계좌 정보를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 특정 계좌 조회 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 비밀번호"),
		@ApiResponse(responseCode = "403", description = "❗️ 계좌 접근 권한 없음"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 계좌"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<AccountDetailResponse> findAccountDetail(
		@Auth AuthMember authMember,
		@PathVariable Long accountId,
		@RequestBody AccountPasswordRequest accountPasswordRequest) {

		return ResponseEntity.ok()
			.body(accountService.findAccountDetail(authMember, accountId, accountPasswordRequest));
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "모든 계좌 정보 조회", description = "사용자 인증 후 모든 계좌 정보를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 모든 계좌 조회 성공"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<List<AccountDetailResponse>> findAllAccounts(@Auth AuthMember authMember) {
		return ResponseEntity.ok().body(accountService.findAllAccounts(authMember));
	}

	@PostMapping("/{accountId}/deposit")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "계좌 입금 처리", description = "계좌 ID와 금액을 받아 계좌에 입금을 진행합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 계좌 입금 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 금액"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 계좌"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<?> depositToAccount(
		@PathVariable Long accountId,
		@RequestHeader(name = "Idempotency-Key") String idempotencyKey,
		@Valid @RequestBody DepositFormRequest depositFormRequest) {

		depositTransactionHandler.depositToAccount(accountId, idempotencyKey, depositFormRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 계좌 입금이 성공적으로 완료되었습니다.");
	}

	@PostMapping("/{accountId}/withdraw")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "계좌 출금 처리", description = "계좌 ID와 금액을 받아 계좌에 출금을 진행합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 계좌 출금 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 금액, 비밀번호"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 계좌"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<?> withdrawToAccount(
		@PathVariable Long accountId,
		@RequestHeader(name = "Idempotency-Key") String idempotencyKey,
		@Valid @RequestBody WithdrawFormRequest withdrawFormRequest) {

		withdrawTransactionHandler.withdrawToAccount(accountId, idempotencyKey, withdrawFormRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 계좌 출금이 성공적으로 완료되었습니다.");
	}

	@PostMapping("/{accountId}/transfer")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "계좌 송금 처리", description = "사용자 인증 후 계좌 ID와 금액을 받아 송금을 진행합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 계좌 송금 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 금액"),
		@ApiResponse(responseCode = "403", description = "❗️ 계좌 접근 권한 없음"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 계좌"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<?> transferToAccount(
		@Auth AuthMember authMember,
		@PathVariable Long accountId,
		@RequestHeader(name = "Idempotency-Key") String idempotencyKey,
		@Valid @RequestBody TransferFormRequest transferFormRequest) {

		transferTransactionHandler.transferToAccount(authMember, accountId, idempotencyKey, transferFormRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 계좌 송금이 성공적으로 완료되었습니다.");
	}

	@PatchMapping("/{accountId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "계좌 상태 수정", description = "사용자 인증 후 계좌 상태를 수정했습니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 계좌 상태 수정 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 비밀번호"),
		@ApiResponse(responseCode = "403", description = "❗️ 계좌 접근 권한 없음"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 계좌"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<?> updateAccountStatus(
		@Auth AuthMember authMember,
		@PathVariable Long accountId,
		@Valid @RequestBody UpdateStatusRequest updateStatusRequest) {

		accountService.updateAccountStatus(authMember, accountId, updateStatusRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 계좌 상태를 성공적으로 수정했습니다.");
	}
}
