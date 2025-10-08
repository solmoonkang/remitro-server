package com.remitro.account.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.dto.request.AccountDepositRequest;
import com.remitro.account.application.dto.request.AccountPasswordRequest;
import com.remitro.account.application.dto.request.AccountWithdrawRequest;
import com.remitro.account.application.dto.request.CreateAccountRequest;
import com.remitro.account.application.dto.request.TransferFormRequest;
import com.remitro.account.application.dto.request.UpdateStatusRequest;
import com.remitro.account.application.dto.response.AccountDetailResponse;
import com.remitro.account.application.mapper.AccountMapper;
import com.remitro.account.application.validator.AccountValidator;
import com.remitro.account.domain.model.Account;
import com.remitro.account.infrastructure.redis.DistributedLockManager;
import com.remitro.common.auth.model.AuthMember;
import com.remitro.member.domain.model.Member;
import com.remitro.member.domain.service.MemberReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

	private final AccountValidator accountValidator;
	private final MemberReadService memberReadService;
	private final AccountWriteService accountWriteService;
	private final AccountReadService accountReadService;
	private final DistributedLockManager distributedLockManager;

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
	public void depositToAccount(Long accountId, AccountDepositRequest accountDepositRequest) {
		accountValidator.validateAmountPositive(accountDepositRequest.amount());
		distributedLockManager.executeProtectedDistributedLock(accountId, () -> {
			final Account receiver = accountReadService.findAccountById(accountId);
			accountWriteService.processDeposit(receiver, accountDepositRequest);
		});
	}

	@Transactional
	public void withdrawToAccount(Long accountId, AccountWithdrawRequest accountWithdrawRequest) {
		accountValidator.validateAmountPositive(accountWithdrawRequest.amount());

		distributedLockManager.executeProtectedDistributedLock(accountId, () -> {
			final Account sender = accountReadService.findAccountById(accountId);
			accountValidator.validateAccountPasswordMatch(accountWithdrawRequest.password(), sender.getPassword());
			accountWriteService.processWithdraw(sender, accountWithdrawRequest);
		});
	}

	@Transactional
	public void transferToAccount(AuthMember authMember, Long accountId, TransferFormRequest transferFormRequest) {
		final Long receiverId = accountReadService.findAccountIdByNumber(transferFormRequest.receiverAccountNumber());
		final Long keyA = Math.min(accountId, receiverId);
		final Long keyB = Math.max(accountId, receiverId);

		distributedLockManager.executeProtectedMultiDistributedLock(keyA, keyB, () -> {
			final Account sender = accountReadService.findAccountById(accountId);
			final Account receiver = accountReadService.findAccountById(receiverId);

			accountValidator.validateAccountAccess(sender.getMember().getId(), authMember.id());
			accountValidator.validateAccountPasswordMatch(transferFormRequest.password(), sender.getPassword());
			accountValidator.validateSufficientBalance(sender.getBalance(), transferFormRequest.amount());
			accountValidator.validateSelfTransfer(sender.getId(), receiver.getId());

			accountWriteService.processTransfer(sender, receiver, transferFormRequest);
		});
	}

	@Transactional
	public void updateAccountStatus(AuthMember authMember, Long accountId, UpdateStatusRequest updateStatusRequest) {
		final Account account = accountReadService.findAccountById(accountId);
		accountValidator.validateAccountAccess(account.getMember().getId(), authMember.id());
		accountValidator.validateAccountPasswordMatch(updateStatusRequest.password(), account.getPassword());
		accountWriteService.updateAccountStatus(account, updateStatusRequest);
	}
}
