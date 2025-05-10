package com.remitroserver.api.presentation;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitroserver.api.application.account.AccountService;
import com.remitroserver.api.domain.auth.model.AuthMember;
import com.remitroserver.api.dto.account.request.AccountAmountRequest;
import com.remitroserver.api.dto.account.request.AccountCreateRequest;
import com.remitroserver.api.dto.account.response.AccountBalanceResponse;
import com.remitroserver.api.dto.account.response.AccountDetailResponse;
import com.remitroserver.api.dto.account.response.AccountSummaryResponse;
import com.remitroserver.global.auth.annotation.Auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
@Tag(name = "계좌 APIs", description = "계좌 생성 및 관리 API")
public class AccountController {

	private final AccountService accountService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "계좌 생성 - 사용자 계좌 생성",
		description = "계좌 타입을 선택해 사용자가 입출금 계좌를 생성합니다. 생성 시 잔액은 0원으로 초기화됩니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "✅ 계좌 생성 완료"),
		@ApiResponse(responseCode = "400", description = "❌ 잘못된 요청 또는 계좌 제한 초과"),
		@ApiResponse(responseCode = "409", description = "⚠️ 중복된 계좌번호 생성 시도"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류 (계좌 번호 생성 실패 등)")
	})
	public ResponseEntity<String> createAccount(
		@Auth AuthMember authMember,
		@RequestBody @Valid AccountCreateRequest accountCreateRequest) {

		accountService.createAccount(authMember, accountCreateRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 사용자 계좌를 성공적으로 생성했습니다.");
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "내 계좌 목록 조회 - 사용자가 보유한 계좌 목록 조회",
		description = "현재 로그인한 사용자가 개설한 모든 계좌 정보를 최신순으로 정렬하여 반환합니다. (잔액, 상태, 생성일 포함)"
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "✅ 계좌 목록 조회 성공"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "404", description = "🔍 해당 사용자 정보를 찾을 수 없음"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<List<AccountSummaryResponse>> findAllMyAccounts(@Auth AuthMember authMember) {
		return ResponseEntity.ok().body(accountService.findAllMyAccounts(authMember));
	}

	@GetMapping("/{accountToken}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "계좌 상세 조회 - 단일 계좌 정보 확인",
		description = "랜덤 UUID 계좌 값을 기반으로 계좌 번호, 잔액, 상태, 소유자 닉네임 등 상세 정보를 반환합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "✅ 계좌 상세 정보 조회 성공"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "403", description = "🚫 접근 권한 부족"),
		@ApiResponse(responseCode = "404", description = "🔍 해당 계좌 정보 또는 사용자 정보를 찾을 수 없음"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<AccountDetailResponse> findAccountDetail(
		@PathVariable UUID accountToken,
		@Auth AuthMember authMember) {

		return ResponseEntity.ok().body(accountService.findAccountDetail(accountToken, authMember));
	}

	@GetMapping("/{accountToken}/balance")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "계좌 잔액 조회 - 현재 잔액 확인",
		description = "UUID 기반 계좌 식별자를 통해 현재 계좌의 잔액을 반환합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "✅ 잔액 조회 성공"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "403", description = "🚫 접근 권한 부족"),
		@ApiResponse(responseCode = "404", description = "🔍 계좌 또는 사용자 정보를 찾을 수 없음"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<AccountBalanceResponse> findAccountBalance(
		@PathVariable UUID accountToken,
		@Auth AuthMember authMember) {

		return ResponseEntity.ok().body(accountService.findAccountBalance(accountToken, authMember));
	}

	@PatchMapping("/{accountToken}/deposit")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "계좌 입금 - 지정된 금액 입금",
		description = "인증된 사용자의 계좌에 지정된 금액을 입금합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "✅ 입금 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 잘못된 금액 요청"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "403", description = "🚫 접근 권한 부족"),
		@ApiResponse(responseCode = "404", description = "🔍 계좌 또는 사용자 정보가 존재하지 않음")
	})
	public ResponseEntity<String> depositToAccount(
		@PathVariable UUID accountToken,
		@Auth AuthMember authMember,
		@RequestBody @Valid AccountAmountRequest accountAmountRequest) {

		accountService.deposit(accountToken, authMember, accountAmountRequest);
		return ResponseEntity.ok().body(String.format(
			"[✅ SUCCESS] 사용자 계좌에 %s원이 성공적으로 입금되었습니다.", String.format("%,d", accountAmountRequest.amount()))
		);
	}

	@PatchMapping("/{accountToken}/withdraw")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "계좌 출금 - 지정된 금액 출금",
		description = "인증된 사용자의 계좌에서 지정된 금액을 출금합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "✅ 출금 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 잘못된 금액 요청 또는 잔액 부족"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "403", description = "🚫 접근 권한 부족"),
		@ApiResponse(responseCode = "404", description = "🔍 계좌 또는 사용자 정보가 존재하지 않음")
	})
	public ResponseEntity<String> withdrawFromAccount(
		@PathVariable UUID accountToken,
		@Auth AuthMember authMember,
		@RequestBody @Valid AccountAmountRequest accountAmountRequest) {

		accountService.withdraw(accountToken, authMember, accountAmountRequest);
		return ResponseEntity.ok().body(String.format(
			"[✅ SUCCESS] 사용자 계좌에 %s원이 성공적으로 출금되었습니다.", String.format("%,d", accountAmountRequest.amount()))
		);
	}

	@PatchMapping("/{accountToken}/suspend")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "계좌 정지 - 사용자 요청에 의한 계좌 일시 정지",
		description = "계좌를 분실했거나 의심 거래가 감지된 경우 계좌를 일시적으로 정지합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "✅ 계좌 정지 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 잘못된 계좌 상태이거나 요청 오류"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "403", description = "🚫 접근 권한 부족"),
		@ApiResponse(responseCode = "404", description = "🔍 계좌 또는 사용자 정보를 찾을 수 없음")
	})
	public ResponseEntity<String> suspendAccount(
		@PathVariable UUID accountToken,
		@Auth AuthMember authMember) {

		accountService.suspendAccount(accountToken, authMember);
		return ResponseEntity.ok().body("[✅ SUCCESS] 계좌가 성공적으로 정지되었습니다.");
	}

	@PatchMapping("/{accountToken}/activate")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "계좌 복구 - 정지된 계좌 복구 요청",
		description = "본인 인증을 완료한 사용자에 한해 정지된 계좌를 다시 활성화합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "✅ 계좌 복구 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 잘못된 계좌 상태이거나 요청 오류"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "403", description = "🚫 접근 권한 부족"),
		@ApiResponse(responseCode = "404", description = "🔍 계좌 또는 사용자 정보를 찾을 수 없음")
	})
	public ResponseEntity<String> activateAccount(
		@PathVariable UUID accountToken,
		@Auth AuthMember authMember) {

		accountService.activateAccount(accountToken, authMember);
		return ResponseEntity.ok().body("[✅ SUCCESS] 계좌가 성공적으로 복구되었습니다.");
	}

	@PatchMapping("/{accountToken}/close")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "계좌 해지 - 계좌 사용 종료 처리",
		description = "잔액이 0원인 계좌에 한해 계좌를 해지할 수 있습니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "✅ 계좌 해지 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 잘못된 계좌 상태이거나 요청 오류"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "403", description = "🚫 접근 권한 부족"),
		@ApiResponse(responseCode = "404", description = "🔍 계좌 또는 사용자 정보를 찾을 수 없음")
	})
	public ResponseEntity<String> closeAccount(
		@PathVariable UUID accountToken,
		@Auth AuthMember authMember) {

		accountService.closeAccount(accountToken, authMember);
		return ResponseEntity.ok().body("[✅ SUCCESS] 계좌가 성공적으로 해지되었습니다.");
	}
}
