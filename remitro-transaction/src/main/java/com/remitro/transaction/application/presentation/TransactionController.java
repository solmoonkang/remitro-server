package com.remitro.transaction.application.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.auth.annotation.Auth;
import com.remitro.common.auth.model.AuthMember;
import com.remitro.transaction.application.dto.response.TransactionDetailResponse;
import com.remitro.transaction.domain.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;

	@GetMapping("/{transactionId}")
	public ResponseEntity<TransactionDetailResponse> findTransactionDetail(
		@Auth AuthMember authMember,
		@PathVariable Long transactionId) {

		return ResponseEntity.ok().body(transactionService.findTransactionDetail(authMember, transactionId));
	}

	@GetMapping("/{accountId}")
	public ResponseEntity<List<TransactionDetailResponse>> findAllTransactions(
		@Auth AuthMember authMember,
		@PathVariable Long accountId) {

		return ResponseEntity.ok().body(transactionService.findAllTransactions(authMember, accountId));
	}
}
