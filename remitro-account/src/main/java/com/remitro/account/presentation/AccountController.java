package com.remitro.account.presentation;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.account.application.dto.request.OpenAccountRequest;
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
@Tag(name = "계좌 APIs", description = "계좌 정보 저장, 송금, 입출금, 조회 관련 API")
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
}
