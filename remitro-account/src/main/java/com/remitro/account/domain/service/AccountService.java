package com.remitro.account.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.dto.request.AccountPasswordRequest;
import com.remitro.account.application.dto.request.CreateAccountRequest;
import com.remitro.account.application.dto.request.UpdateStatusRequest;
import com.remitro.account.application.dto.response.AccountDetailResponse;
import com.remitro.account.application.mapper.AccountMapper;
import com.remitro.account.application.validator.AccountValidator;
import com.remitro.account.domain.model.Account;
import com.remitro.common.auth.model.AuthMember;
import com.remitro.member.domain.model.Member;
import com.remitro.member.domain.service.MemberReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

	// TODO: 이벤트 발행의 롤백 위험 (process... 트랜잭션 내에서 이벤트 기록 실패 시 금융 거래 전체 롤백 위험)

	private final AccountValidator accountValidator;
	private final MemberReadService memberReadService;
	private final AccountWriteService accountWriteService;
	private final AccountReadService accountReadService;

	@Transactional
	public void createAccount(AuthMember authMember, CreateAccountRequest createAccountRequest) {
		final Member member = memberReadService.findMemberById(authMember.id());
		accountWriteService.saveAccount(member, createAccountRequest);
	}

	public AccountDetailResponse findAccountDetail(AuthMember authMember, Long accountId,
		AccountPasswordRequest accountPasswordRequest) {

		final Account account = accountReadService.findAccountById(accountId);
		accountValidator.validateAccountAccess(account.getMember().getId(), authMember.id());
		accountValidator.validateAccountPasswordMatch(accountPasswordRequest.password(), account.getPassword());
		return AccountMapper.toAccountDetailResponse(account);
	}

	public List<AccountDetailResponse> findAllAccounts(AuthMember authMember) {
		final List<Account> accounts = accountReadService.findAllAccountsByMemberId(authMember.id());
		return AccountMapper.toAccountListResponse(accounts);
	}

	@Transactional
	public void updateAccountStatus(AuthMember authMember, Long accountId, UpdateStatusRequest updateStatusRequest) {
		final Account account = accountReadService.findAccountById(accountId);
		accountValidator.validateAccountAccess(account.getMember().getId(), authMember.id());
		accountValidator.validateAccountPasswordMatch(updateStatusRequest.password(), account.getPassword());
		accountWriteService.updateAccountStatus(account, updateStatusRequest);
	}
}
