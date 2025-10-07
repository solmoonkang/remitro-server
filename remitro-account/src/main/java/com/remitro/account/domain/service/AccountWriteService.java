package com.remitro.account.domain.service;

import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.dto.request.AccountDepositRequest;
import com.remitro.account.application.dto.request.AccountWithdrawRequest;
import com.remitro.account.application.dto.request.CreateAccountRequest;
import com.remitro.account.application.dto.request.CreatePublishedEventRequest;
import com.remitro.account.application.dto.request.TransferFormRequest;
import com.remitro.account.application.dto.request.UpdateStatusRequest;
import com.remitro.account.application.mapper.EventMapper;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.enums.AccountStatus;
import com.remitro.account.domain.model.enums.AccountType;
import com.remitro.account.domain.repository.AccountRepository;
import com.remitro.common.common.entity.enums.EventType;
import com.remitro.common.common.event.DepositEventMessage;
import com.remitro.common.common.event.TransferEventMessage;
import com.remitro.common.common.event.WithdrawEventMessage;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountWriteService {

	public static final int RANDOM_ACCOUNT_NUMBER_LENGTH = 8;

	private final PasswordEncoder passwordEncoder;
	private final AccountRepository accountRepository;
	private final PublishedEventService publishedEventService;

	public void saveAccount(Member member, CreateAccountRequest createAccountRequest) {
		final AccountType accountType = AccountType.valueOf(createAccountRequest.accountType().toUpperCase());
		final String accountNumber = accountType.getCode() + generateRandomAccountNumber();
		final String encodedPassword = passwordEncoder.encode(createAccountRequest.password());

		final Account account = Account.createAccount(
			member, accountNumber, createAccountRequest.accountName(), encodedPassword, accountType);
		accountRepository.save(account);
	}

	public void updateAccountStatus(Account account, UpdateStatusRequest updateStatusRequest) {
		final AccountStatus newAccountStatus = AccountStatus.valueOf(updateStatusRequest.accountStatus().toUpperCase());
		account.updateAccountStatus(newAccountStatus);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processDeposit(Account receiver, AccountDepositRequest accountDepositRequest) {
		receiver.deposit(accountDepositRequest.amount());
		final DepositEventMessage depositEventMessage = EventMapper.toDepositEventMessage(
			receiver.getAccountNumber(), accountDepositRequest.amount());
		recordPublishedEventForAccount(receiver, EventType.DEPOSIT_EVENT, depositEventMessage);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processWithdraw(Account sender, AccountWithdrawRequest accountWithdrawRequest) {
		sender.withdraw(accountWithdrawRequest.amount());
		final WithdrawEventMessage withdrawEventMessage = EventMapper.toWithdrawEventMessage(
			sender.getAccountNumber(), accountWithdrawRequest.amount());
		recordPublishedEventForAccount(sender, EventType.WITHDRAWAL_EVENT, withdrawEventMessage);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processTransfer(Account sender, Account receiver, TransferFormRequest transferFormRequest) {
		sender.withdraw(transferFormRequest.amount());
		receiver.deposit(transferFormRequest.amount());
		final TransferEventMessage transferEventMessage = EventMapper.toTransferEventMessage(
			sender, receiver, transferFormRequest);
		recordPublishedEventForAccount(sender, EventType.TRANSFER_EVENT, transferEventMessage);
	}

	private String generateRandomAccountNumber() {
		Random random = new Random();
		StringBuilder randomNumberBuilder = new StringBuilder();

		for (int i = 0; i < RANDOM_ACCOUNT_NUMBER_LENGTH; i++) {
			randomNumberBuilder.append(random.nextInt(10));
		}

		return randomNumberBuilder.toString();
	}

	private <T> void recordPublishedEventForAccount(Account account, EventType eventType, T eventMessage) {
		final CreatePublishedEventRequest createPublishedEventRequest = EventMapper.toCreatePublishedEventRequest(
			account, eventType, eventMessage);
		publishedEventService.recordPublishedEvent(createPublishedEventRequest);
	}
}
