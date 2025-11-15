package com.remitro.account.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.read.MemberProjection;
import com.remitro.account.domain.repository.AccountRepository;
import com.remitro.account.domain.repository.MemberProjectionRepository;
import com.remitro.common.infra.error.exception.NotFoundException;
import com.remitro.common.infra.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountReadService {

	private final AccountRepository accountRepository;
	private final MemberProjectionRepository memberProjectionRepository;

	public MemberProjection findMemberProjectionById(Long memberId) {
		return memberProjectionRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND));
	}

	public Account findAccountById(Long accountId) {
		return accountRepository.findByAccountId(accountId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.ACCOUNT_NOT_FOUND));
	}

	public List<Account> findAllAccountsByMemberId(Long memberId) {
		return accountRepository.findByMemberId(memberId);
	}

	public Account findAccountByNumber(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.ACCOUNT_NOT_FOUND));
	}

	public Long findAccountIdByNumber(String accountNumber) {
		return accountRepository.findAccountIdByAccountNumber(accountNumber)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.ACCOUNT_NOT_FOUND));
	}
}
