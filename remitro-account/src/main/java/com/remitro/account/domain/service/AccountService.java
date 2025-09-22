package com.remitro.account.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.dto.request.AccountDepositRequest;
import com.remitro.account.application.dto.request.AccountPasswordRequest;
import com.remitro.account.application.dto.request.AccountWithdrawRequest;
import com.remitro.account.application.dto.request.CreateAccountRequest;
import com.remitro.account.application.dto.request.CreatePublishedEventRequest;
import com.remitro.account.application.dto.request.TransferFormRequest;
import com.remitro.account.application.dto.request.UpdateStatusRequest;
import com.remitro.account.application.dto.response.AccountDetailResponse;
import com.remitro.account.application.mapper.AccountMapper;
import com.remitro.account.application.mapper.EventMapper;
import com.remitro.account.application.validator.AccountValidator;
import com.remitro.account.domain.model.Account;
import com.remitro.common.auth.model.AuthMember;
import com.remitro.common.common.entity.enums.EventType;
import com.remitro.common.common.event.DepositEventMessage;
import com.remitro.common.common.event.TransferEventMessage;
import com.remitro.common.common.event.WithdrawEventMessage;
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
	private final PublishedEventService publishedEventService;

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
		final Account receiver = accountReadService.findAccountById(accountId);
		receiver.deposit(accountDepositRequest.amount());

		final DepositEventMessage depositEventMessage = EventMapper.toDepositEventMessage(
			receiver.getAccountNumber(), accountDepositRequest.amount());
		recordPublishedEventForAccount(receiver, EventType.DEPOSIT_EVENT, depositEventMessage);
	}

	@Transactional
	public void withdrawToAccount(Long accountId, AccountWithdrawRequest accountWithdrawRequest) {
		accountValidator.validateAmountPositive(accountWithdrawRequest.amount());
		final Account sender = accountReadService.findAccountById(accountId);
		accountValidator.validateAccountPasswordMatch(accountWithdrawRequest.password(), sender.getPassword());
		sender.withdraw(accountWithdrawRequest.amount());

		final WithdrawEventMessage withdrawEventMessage = EventMapper.toWithdrawEventMessage(
			sender.getAccountNumber(), accountWithdrawRequest.amount());
		recordPublishedEventForAccount(sender, EventType.WITHDRAWAL_EVENT, withdrawEventMessage);
	}

	@Transactional
	public void transferToAccount(AuthMember authMember, Long accountId, TransferFormRequest transferFormRequest) {
		final Account sender = findAndLockSenderAccount(accountId, transferFormRequest.receiverAccountNumber());
		final Account receiver = findAndLockReceiverAccount(accountId, transferFormRequest.receiverAccountNumber());

		accountValidator.validateAccountAccess(sender.getMember().getId(), authMember.id());
		accountValidator.validateAccountPasswordMatch(transferFormRequest.password(), sender.getPassword());
		accountValidator.validateSufficientBalance(sender.getBalance(), transferFormRequest.amount());
		accountValidator.validateSelfTransfer(sender.getId(), receiver.getId());

		sender.withdraw(transferFormRequest.amount());
		receiver.deposit(transferFormRequest.amount());

		final TransferEventMessage transferEventMessage = EventMapper.toTransferEventMessage(
			sender, receiver, transferFormRequest);
		recordPublishedEventForAccount(sender, EventType.TRANSFER_EVENT, transferEventMessage);
	}

	@Transactional
	public void updateAccountStatus(AuthMember authMember, Long accountId, UpdateStatusRequest updateStatusRequest) {
		final Account account = accountReadService.findAccountById(accountId);
		accountValidator.validateAccountAccess(account.getMember().getId(), authMember.id());
		accountValidator.validateAccountPasswordMatch(updateStatusRequest.password(), account.getPassword());
		accountWriteService.updateAccountStatus(account, updateStatusRequest);
	}

	private <T> void recordPublishedEventForAccount(Account account, EventType eventType, T eventMessage) {
		final CreatePublishedEventRequest createPublishedEventRequest = EventMapper.toCreatePublishedEventRequest(
			account, eventType, eventMessage);
		publishedEventService.recordPublishedEvent(createPublishedEventRequest);
	}

	private Account findAndLockSenderAccount(Long senderAccountId, String receiverAccountNumber) {
		final Long receiverAccountId = accountReadService.findAccountIdByNumber(receiverAccountNumber);

		if (senderAccountId < receiverAccountId) {
			return accountReadService.findAccountById(senderAccountId);
		}

		accountReadService.findAccountById(receiverAccountId);
		return accountReadService.findAccountById(senderAccountId);
	}

	private Account findAndLockReceiverAccount(Long senderAccountId, String receiverAccountNumber) {
		final Long receiverAccountId = accountReadService.findAccountIdByNumber(receiverAccountNumber);

		if (senderAccountId < receiverAccountId) {
			accountReadService.findAccountById(senderAccountId);
			return accountReadService.findAccountById(receiverAccountId);
		}

		return accountReadService.findAccountById(receiverAccountId);
	}
}
