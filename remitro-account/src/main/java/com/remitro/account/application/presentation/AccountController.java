package com.remitro.account.application.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.account.application.dto.request.AccountDepositRequest;
import com.remitro.account.application.dto.request.AccountPasswordRequest;
import com.remitro.account.application.dto.request.AccountWithdrawRequest;
import com.remitro.account.application.dto.request.CreateAccountRequest;
import com.remitro.account.application.dto.request.TransferFormRequest;
import com.remitro.account.application.dto.request.UpdateStatusRequest;
import com.remitro.account.application.dto.response.AccountDetailResponse;
import com.remitro.account.domain.service.AccountService;
import com.remitro.common.auth.annotation.Auth;
import com.remitro.common.auth.model.AuthMember;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@PostMapping
	public ResponseEntity<?> createAccount(
		@Auth AuthMember authMember,
		@Valid @RequestBody CreateAccountRequest createAccountRequest) {

		accountService.createAccount(authMember, createAccountRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 계좌 생성이 성공적으로 완료되었습니다.");
	}

	@PostMapping("/{accountId}")
	public ResponseEntity<AccountDetailResponse> findAccountDetail(
		@Auth AuthMember authMember,
		@PathVariable Long accountId,
		@RequestBody AccountPasswordRequest accountPasswordRequest) {

		return ResponseEntity.ok()
			.body(accountService.findAccountDetail(authMember, accountId, accountPasswordRequest));
	}

	@GetMapping
	public ResponseEntity<List<AccountDetailResponse>> findAllAccounts(@Auth AuthMember authMember) {
		return ResponseEntity.ok().body(accountService.findAllAccounts(authMember));
	}

	@PostMapping("/{accountId}/deposit")
	public ResponseEntity<?> depositToAccount(
		@PathVariable Long accountId,
		@Valid @RequestBody AccountDepositRequest accountDepositRequest) {

		accountService.depositToAccount(accountId, accountDepositRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 계좌 입금이 성공적으로 완료되었습니다.");
	}

	@PostMapping("/{accountId}/withdraw")
	public ResponseEntity<?> withdrawToAccount(
		@PathVariable Long accountId,
		@Valid @RequestBody AccountWithdrawRequest accountWithdrawRequest) {

		accountService.withdrawToAccount(accountId, accountWithdrawRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 계좌 출금이 성공적으로 완료되었습니다.");
	}

	@PostMapping("/{accountId}/transfer")
	public ResponseEntity<?> transferToAccount(
		@Auth AuthMember authMember,
		@PathVariable Long accountId,
		@Valid @RequestBody TransferFormRequest transferFormRequest) {

		accountService.transferToAccount(authMember, accountId, transferFormRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 계좌 송금이 성공적으로 완료되었습니다.");
	}

	@PatchMapping("/{accountId}")
	public ResponseEntity<?> updateAccountStatus(
		@Auth AuthMember authMember,
		@PathVariable Long accountId,
		@Valid @RequestBody UpdateStatusRequest updateStatusRequest) {

		accountService.updateAccountStatus(authMember, accountId, updateStatusRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 계좌 상태를 성공적으로 수정했습니다.");
	}
}
