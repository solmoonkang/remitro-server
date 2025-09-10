package com.remitro.account.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.repository.AccountRepository;
import com.remitro.common.error.exception.NotFoundException;
import com.remitro.common.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountReadService {

	private final AccountRepository accountRepository;

	public Account findAccountById(Long id) {
		return accountRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.ACCOUNT_NOT_FOUND));
	}

	public List<Account> findAllAccountsByMemberId(Long memberId) {
		return accountRepository.findByMemberId(memberId);
	}
}
