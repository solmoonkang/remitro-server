package com.remitro.account.presentation;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.account.application.dto.request.OpenAccountRequest;
import com.remitro.account.application.dto.request.deposit.DepositCommand;
import com.remitro.account.application.dto.request.deposit.DepositRequest;
import com.remitro.account.application.dto.response.AccountBalanceResponse;
import com.remitro.account.application.dto.response.AccountDetailResponse;
import com.remitro.account.application.dto.response.AccountsSummaryResponse;
import com.remitro.account.application.dto.response.DepositResponse;
import com.remitro.account.application.dto.response.OpenAccountCreationResponse;
import com.remitro.account.application.mapper.AccountMapper;
import com.remitro.account.application.service.AccountService;
import com.remitro.account.domain.model.enums.AccountStatus;
import com.remitro.account.infrastructure.auth.LoginMemberId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "계좌 APIs", description = "계좌 개설 및 조회, 입/출금 관련 API")
public class AccountController {

	private final AccountService accountService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "계좌 개설", description = "사용자 인증 후 계좌 정보를 개설했습니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "🎉 계좌 개설 성공"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<OpenAccountCreationResponse> openAccount(
		@LoginMemberId Long memberId,
		@RequestHeader(name = "Idempotency-Key", required = false) String idempotencyKey,
		@Valid @RequestBody OpenAccountRequest openAccountRequest
	) {
		OpenAccountCreationResponse openAccountCreationResponse = accountService.openAccount(
			memberId,
			idempotencyKey,
			openAccountRequest
		);

		return ResponseEntity
			.created(URI.create("/api/accounts/" + openAccountCreationResponse.accountId()))
			.body(openAccountCreationResponse);
	}

	@GetMapping("/{accountId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "단일 계좌 상세 조회", description = "사용자 인증 후 단일 계좌 정보를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 단일 계좌 정보 조회 성공"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<AccountDetailResponse> getAccountDetail(
		@LoginMemberId Long memberId,
		@PathVariable Long accountId
	) {
		return ResponseEntity.ok().body(accountService.findAccountDetail(memberId, accountId));
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "회원 보유 계좌 목록 조회", description = "사용자 인증 후 회원이 보유한 전체 계좌 목록을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 회원 보유 계좌 목록 조회 성공"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<AccountsSummaryResponse> getMemberAccounts(@LoginMemberId Long memberId) {
		return ResponseEntity.ok().body(accountService.findMemberAccounts(memberId));
	}

	@GetMapping("/{accountId}/balance")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "계좌 잔액 조회", description = "사용자 인증 후 특정 계좌의 잔액을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 계좌 잔액 조회 성공"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자 또는 계좌"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<AccountBalanceResponse> getAccountBalance(
		@LoginMemberId Long memberId,
		@PathVariable Long accountId
	) {
		return ResponseEntity.ok().body(accountService.findAccountBalance(memberId, accountId));
	}

	@PatchMapping("/{accountId}/status")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "계좌 상태 변경", description = "이전 계좌 상태를 새로운 계좌 상태로 업데이트했습니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 계좌 상태 변경 성공"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 계좌"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<?> changeAccountStatus(
		@PathVariable Long accountId,
		@RequestParam AccountStatus accountStatus
	) {
		accountService.changeAccountStatus(accountId, accountStatus);
		return ResponseEntity.ok().body("[✅ SUCCESS] 계좌 상태 변경을 성공적으로 완료했습니다.");
	}

	@PostMapping("/{accountId}/deposit")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "입금 요청", description = "사용자 인증 후 입금을 진행합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 입금 요청 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 잘못된 요청 또는 유효성 검사 실패"),
		@ApiResponse(responseCode = "403", description = "❗️ 계좌 접근 권한 없음"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 계좌"),
		@ApiResponse(responseCode = "409", description = "⚠️ 멱등성 충돌 또는 중복 요청"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<DepositResponse> deposit(
		@LoginMemberId Long memberId,
		@RequestHeader(name = "Idempotency-Key", required = false) String idempotencyKey,
		@PathVariable Long accountId,
		@Valid @RequestBody DepositRequest depositRequest
	) {
		DepositCommand depositCommand = AccountMapper.toDepositCommand(
			memberId,
			idempotencyKey,
			accountId,
			depositRequest
		);

		return ResponseEntity.ok().body(accountService.deposit(depositCommand));
	}
}
