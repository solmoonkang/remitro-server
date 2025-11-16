package com.remitro.account.presentation;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.account.application.dto.request.OpenAccountRequest;
import com.remitro.account.application.dto.response.AccountDetailResponse;
import com.remitro.account.application.dto.response.OpenAccountCreationResponse;
import com.remitro.account.application.service.AccountService;
import com.remitro.common.infra.auth.annotation.Auth;
import com.remitro.common.infra.auth.model.AuthMember;

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
		@Auth AuthMember authMember,
		@RequestHeader(name = "Idempotency-Key", required = false) String idempotencyKey,
		@Valid @RequestBody OpenAccountRequest openAccountRequest) {

		OpenAccountCreationResponse openAccountCreationResponse = accountService.openAccount(
			authMember.id(),
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
	public ResponseEntity<AccountDetailResponse> findAccountDetail(
		@Auth AuthMember authMember,
		@PathVariable Long accountId) {

		return ResponseEntity.ok().body(accountService.findAccountDetail(authMember.id(), accountId));
	}
}
