package com.remitroserver.api.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitroserver.api.application.account.AccountService;
import com.remitroserver.api.domain.auth.model.AuthMember;
import com.remitroserver.api.dto.account.request.AccountCreateRequest;
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
		summary = "계좌 생성",
		description = "계좌 타입을 선택해 사용자가 입출금 계좌를 생성합니다. 생성 시 잔액은 0원으로 초기화됩니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "✅ 계좌 생성 완료"),
		@ApiResponse(responseCode = "400", description = "❌ 요청 오류 또는 계좌 제한 초과"),
		@ApiResponse(responseCode = "409", description = "⚠️ 중복된 계좌번호 생성 시도"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류 (계좌 번호 생성 실패 등)")
	})
	public ResponseEntity<String> createAccount(
		@Auth AuthMember authMember,
		@RequestBody @Valid AccountCreateRequest accountCreateRequest) {

		accountService.createAccount(authMember, accountCreateRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 사용자 계좌를 성공적으로 생성했습니다.");
	}
}
