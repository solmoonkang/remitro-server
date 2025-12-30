package com.remitro.account.presentation.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.account.application.usecase.open.dto.request.OpenDepositRequest;
import com.remitro.account.application.usecase.open.dto.request.OpenLoanRequest;
import com.remitro.account.application.usecase.open.dto.request.OpenVirtualRequest;
import com.remitro.account.application.usecase.open.dto.response.OpenAccountCreationResponse;
import com.remitro.account.application.usecase.open.service.AccountOpenCommandService;
import com.remitro.account.application.usecase.query.dto.response.AccountDetailResponse;
import com.remitro.account.application.usecase.query.dto.response.AccountSummaryResponse;
import com.remitro.account.application.usecase.query.service.AccountQueryService;
import com.remitro.common.security.AuthenticatedUser;
import com.remitro.common.security.CurrentUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "계좌 APIs", description = "계좌 개설 및 조회 관련 API")
public class AccountController {

	private final AccountOpenCommandService accountOpenCommandService;
	private final AccountQueryService accountQueryService;

	@PostMapping("/deposit")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "입출금 계좌 개설",
		description = "사용자 인증 후 입출금 계좌를 개설합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "201",
			description = "🎉 입출금 계좌 개설 성공",
			content = @Content(schema = @Schema(implementation = OpenAccountCreationResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 요청"),
		@ApiResponse(responseCode = "409", description = "⚠️ 계좌 개설 정책 위반"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public OpenAccountCreationResponse openDepositAccount(
		@CurrentUser AuthenticatedUser authenticatedUser,
		@RequestHeader(name = "Idempotency-Key") @Parameter(description = "멱등성 키", required = true) String idempotencyKey,
		@Valid @RequestBody OpenDepositRequest openDepositRequest
	) {
		return accountOpenCommandService.openDepositAccount(
			authenticatedUser.memberId(), idempotencyKey, openDepositRequest
		);
	}

	@PostMapping("/loan")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "대출 계좌 개설",
		description = "사용자 인증 후 대출 계좌를 개설합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "201",
			description = "🎉 대출 계좌 개설 성공",
			content = @Content(schema = @Schema(implementation = OpenAccountCreationResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 요청"),
		@ApiResponse(responseCode = "409", description = "⚠️ 활성 대출 계좌 존재"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public OpenAccountCreationResponse openLoanAccount(
		@CurrentUser AuthenticatedUser authenticatedUser,
		@RequestHeader(name = "Idempotency-Key") @Parameter(description = "멱등성 키", required = true) String idempotencyKey,
		@Valid @RequestBody OpenLoanRequest openLoanRequest
	) {
		return accountOpenCommandService.openLoanAccount(
			authenticatedUser.memberId(), idempotencyKey, openLoanRequest
		);
	}

	@PostMapping("/virtual")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "가상 계좌 개설",
		description = "사용자 인증 후 가상 계좌를 개설합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "201",
			description = "🎉 가상 계좌 개설 성공",
			content = @Content(schema = @Schema(implementation = OpenAccountCreationResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 요청"),
		@ApiResponse(responseCode = "409", description = "⚠️ 가상 계좌 개설 불가 상태"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public OpenAccountCreationResponse openVirtualAccount(
		@CurrentUser AuthenticatedUser authenticatedUser,
		@RequestHeader(name = "Idempotency-Key") @Parameter(description = "멱등성 키", required = true) String idempotencyKey,
		@Valid @RequestBody OpenVirtualRequest openVirtualRequest
	) {
		return accountOpenCommandService.openVirtualAccount(
			authenticatedUser.memberId(), idempotencyKey, openVirtualRequest
		);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "내 계좌 목록 조회",
		description = "로그인한 사용자의 계좌 목록을 조회합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "🎉 계좌 목록 조회 성공",
			content = @Content(
				array = @ArraySchema(schema = @Schema(implementation = AccountSummaryResponse.class))
			)
		),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public List<AccountSummaryResponse> getMyAccounts(@CurrentUser AuthenticatedUser authenticatedUser) {
		return accountQueryService.getMyAllAccount(authenticatedUser.memberId());
	}

	@GetMapping("/{accountId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "계좌 상세 조회",
		description = "로그인한 사용자의 특정 계좌 상세 정보를 조회합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "🎉 계좌 상세 조회 성공",
			content = @Content(schema = @Schema(implementation = AccountDetailResponse.class))
		),
		@ApiResponse(responseCode = "403", description = "❗️ 계좌 접근 권한 없음"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 계좌"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public AccountDetailResponse getAccount(
		@CurrentUser AuthenticatedUser authenticatedUser,
		@PathVariable Long accountId
	) {
		return accountQueryService.getMyAccount(authenticatedUser.memberId(), accountId);
	}
}
