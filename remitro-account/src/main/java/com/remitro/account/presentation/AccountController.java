package com.remitro.account.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.account.application.command.dto.request.AccountOpenRequest;
import com.remitro.account.application.command.dto.request.DepositRequest;
import com.remitro.account.application.command.dto.response.AccountOpenResponse;
import com.remitro.account.application.command.dto.response.DepositResponse;
import com.remitro.account.application.command.account.AccountOpenCommandService;
import com.remitro.account.application.command.transaction.DepositCommandService;
import com.remitro.support.response.CommonResponse;
import com.remitro.support.security.AuthenticatedUser;
import com.remitro.support.security.CurrentUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "ACCOUNT", description = "계좌 개설 및 관리 관련 API")
public class AccountController {

	private final AccountOpenCommandService accountOpenCommandService;
	private final DepositCommandService depositCommandService;

	@Operation(
		summary = "계좌 개설",
		description = "로그인한 사용자의 신규 계좌를 개설합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "계좌 개설 성공"),
		@ApiResponse(responseCode = "400", description = "요청 값 검증 실패"),
		@ApiResponse(responseCode = "403", description = "권한 없는 사용자"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@PostMapping("/open")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<AccountOpenResponse> open(
		@Parameter(hidden = true) @CurrentUser AuthenticatedUser authenticatedUser,
		@RequestHeader("X-Request-ID") String requestId,
		@Valid @RequestBody AccountOpenRequest accountOpenRequest
	) {
		return CommonResponse.success(
			accountOpenCommandService.open(authenticatedUser.memberId(), requestId, accountOpenRequest)
		);
	}

	@Operation(
		summary = "계좌 입금",
		description = "로그인한 사용자가 지정한 계좌에 금액을 입금합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "입금 성공"),
		@ApiResponse(responseCode = "400", description = "요청 값 검증 실패"),
		@ApiResponse(responseCode = "403", description = "권한 없는 사용자"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@PostMapping("/{accountId}/deposit")
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<DepositResponse> deposit(
		@Parameter(hidden = true) @CurrentUser AuthenticatedUser authenticatedUser,
		@PathVariable Long accountId,
		@RequestHeader("X-Request-ID") String requestId,
		@Valid @RequestBody DepositRequest depositRequest
	) {
		return CommonResponse.success(
			depositCommandService.deposit(authenticatedUser.memberId(), accountId, requestId, depositRequest)
		);
	}
}
