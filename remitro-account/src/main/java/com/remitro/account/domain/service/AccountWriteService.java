package com.remitro.account.domain.service;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.dto.request.CreateAccountRequest;
import com.remitro.account.application.dto.request.CreatePublishedEventRequest;
import com.remitro.account.application.dto.request.DepositFormRequest;
import com.remitro.account.application.dto.request.TransferFormRequest;
import com.remitro.account.application.dto.request.UpdateStatusRequest;
import com.remitro.account.application.dto.request.WithdrawFormRequest;
import com.remitro.account.application.mapper.EventMapper;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.enums.AccountStatus;
import com.remitro.account.domain.model.enums.AccountType;
import com.remitro.account.domain.repository.AccountRepository;
import com.remitro.common.common.entity.enums.EventType;
import com.remitro.common.common.event.DepositEventMessage;
import com.remitro.common.common.event.TransferEventMessage;
import com.remitro.common.common.event.WithdrawEventMessage;
import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.model.ErrorMessage;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountWriteService {

	public static final int RANDOM_ACCOUNT_NUMBER_LENGTH = 10;
	public static final int MAX_RETRY_ATTEMPTS = 3;

	private final PasswordEncoder passwordEncoder;
	private final AccountRepository accountRepository;
	private final PublishedEventService publishedEventService;

	public void saveAccount(Member member, CreateAccountRequest createAccountRequest) {
		final AccountType accountType = AccountType.valueOf(createAccountRequest.accountType().toUpperCase());
		final String encodedPassword = passwordEncoder.encode(createAccountRequest.password());
		retryAccountCreation(member, createAccountRequest, accountType, encodedPassword);
	}

	public void updateAccountStatus(Account account, UpdateStatusRequest updateStatusRequest) {
		final AccountStatus newAccountStatus = AccountStatus.valueOf(updateStatusRequest.accountStatus().toUpperCase());
		account.updateAccountStatus(newAccountStatus);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processDeposit(Account receiver, DepositFormRequest depositFormRequest) {
		receiver.deposit(depositFormRequest.amount());

		final DepositEventMessage eventMessage = EventMapper.toDepositEventMessage(
			receiver.getAccountNumber(), depositFormRequest.amount());
		recordPublishedEventForAccount(receiver, EventType.DEPOSIT_EVENT, eventMessage);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processWithdraw(Account sender, WithdrawFormRequest withdrawFormRequest) {
		sender.withdraw(withdrawFormRequest.amount());

		final WithdrawEventMessage eventMessage = EventMapper.toWithdrawEventMessage(
			sender.getAccountNumber(), withdrawFormRequest.amount());
		recordPublishedEventForAccount(sender, EventType.WITHDRAWAL_EVENT, eventMessage);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processTransfer(Account sender, Account receiver, TransferFormRequest transferFormRequest) {
		sender.withdraw(transferFormRequest.amount());
		receiver.deposit(transferFormRequest.amount());

		final TransferEventMessage eventMessage = EventMapper.toTransferEventMessage(
			sender, receiver, transferFormRequest);
		recordPublishedEventForAccount(sender, EventType.TRANSFER_EVENT, eventMessage);
	}

	private String generateRandomAccountNumber() {
		StringBuilder randomNumberBuilder = new StringBuilder();

		for (int i = 0; i < RANDOM_ACCOUNT_NUMBER_LENGTH; i++) {
			randomNumberBuilder.append(ThreadLocalRandom.current().nextInt(10));
		}

		return randomNumberBuilder.toString();
	}

	private void retryAccountCreation(Member member, CreateAccountRequest createAccountRequest, AccountType accountType,
		String encodedPassword) {

		for (int i = 0; true; i++) {
			final String accountNumber = accountType.getCode() + generateRandomAccountNumber();

			try {
				final Account account = Account.createAccount(
					member, accountNumber, createAccountRequest.accountName(), encodedPassword, accountType);
				accountRepository.save(account);
				return;
			} catch (DataIntegrityViolationException e) {
				if (i == MAX_RETRY_ATTEMPTS - 1) {
					throw new ConflictException(ErrorMessage.ACCOUNT_NUMBER_COLLISION);
				}
			}
		}
	}

	private <T> void recordPublishedEventForAccount(Account account, EventType eventType, T eventMessage) {
		final CreatePublishedEventRequest createPublishedEventRequest = EventMapper.toCreatePublishedEventRequest(
			account, eventType, eventMessage);
		publishedEventService.recordPublishedEvent(createPublishedEventRequest);
	}
}
