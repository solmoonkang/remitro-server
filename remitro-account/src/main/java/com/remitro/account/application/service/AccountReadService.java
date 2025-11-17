package com.remitro.account.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.MemberProjection;
import com.remitro.account.domain.repository.AccountRepository;
import com.remitro.account.domain.repository.MemberProjectionRepository;
import com.remitro.common.infra.error.exception.NotFoundException;
import com.remitro.common.infra.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountReadService {

	private final MemberProjectionRepository memberProjectionRepository;
	private final AccountRepository accountRepository;

	public MemberProjection findMemberProjectionById(Long memberId) {
		return memberProjectionRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND));
	}

	public Account findAccountByIdAndMemberId(Long memberId, Long accountId) {
		return accountRepository.findByIdAndMemberId(memberId, accountId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.ACCOUNT_NOT_FOUND));
	}

	public List<Account> findAllAccountByMemberId(Long memberId) {
		return accountRepository.findAccountsByMemberId(memberId);
	}
}
