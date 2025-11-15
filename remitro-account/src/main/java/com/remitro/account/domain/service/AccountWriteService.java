package com.remitro.account.domain.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.dto.request.OpenAccountRequest;
import com.remitro.account.application.dto.request.OutboxMessageRequest;
import com.remitro.account.application.dto.request.DepositFormRequest;
import com.remitro.account.application.dto.request.TransferFormRequest;
import com.remitro.account.application.dto.request.UpdateStatusRequest;
import com.remitro.account.application.dto.request.WithdrawFormRequest;
import com.remitro.account.application.mapper.EventMapper;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.OutboxMessage;
import com.remitro.account.domain.model.enums.AccountStatus;
import com.remitro.account.domain.model.enums.AccountType;
import com.remitro.account.domain.model.read.MemberProjection;
import com.remitro.account.domain.repository.AccountRepository;
import com.remitro.account.domain.repository.OutboxMessageRepository;
import com.remitro.account.domain.service.support.OutboxMessageHandler;
import com.remitro.common.contract.account.AccountOpenedEvent;
import com.remitro.common.domain.enums.AggregateType;
import com.remitro.common.domain.enums.EventType;
import com.remitro.common.infra.error.exception.InternalServerException;
import com.remitro.common.infra.error.model.ErrorMessage;
import com.remitro.common.infra.util.JsonUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountWriteService {

	public static final int RANDOM_ACCOUNT_NUMBER_LENGTH = 10;
	public static final int MAX_RETRY_ATTEMPTS = 3;
	private static final int MAX_ACCOUNT_NUMBER_ATTEMPTS = 5;
	private static final DateTimeFormatter ACCOUNT_NUMBER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

	private final PasswordEncoder passwordEncoder;
	private final AccountRepository accountRepository;
	private final OutboxMessageHandler outboxMessageHandler;
	private final OutboxMessageRepository outboxMessageRepository;

	public Account saveAccount(MemberProjection member, OpenAccountRequest openAccountRequest) {
		final String accountNumber = generateUniqueAccountNumber(openAccountRequest.accountType());
		final String encodedPassword = passwordEncoder.encode(openAccountRequest.password());
		final Account account = Account.create(
			member.getMemberId(),
			accountNumber,
			openAccountRequest.accountName(),
			encodedPassword,
			openAccountRequest.accountType()
		);

		return accountRepository.save(account);
	}

	public void appendAccountOpenedEventOutbox(Account account) {
		final AccountOpenedEvent accountOpendEvent = new AccountOpenedEvent(
			account.getId(),
			account.getMemberId(),
			account.getAccountType().getCode()
		);
		final String eventMessage = JsonUtil.toJSON(accountOpendEvent);

		final OutboxMessage outboxMessage = OutboxMessage.create(
			account.getId(),
			AggregateType.ACCOUNT,
			EventType.ACCOUNT_OPENED,
			eventMessage
		);

		outboxMessageRepository.save(outboxMessage);
	}

	private String generateUniqueAccountNumber(AccountType accountType) {
		for (int attempt = 0; attempt < MAX_ACCOUNT_NUMBER_ATTEMPTS; attempt++) {
			String accountNumber = accountType.getCode()
				+ LocalDateTime.now().format(ACCOUNT_NUMBER_FORMAT)
				+ RandomStringUtils.randomNumeric(5);

			if (!accountRepository.existsByAccountNumber(accountNumber))
				return accountNumber;
		}

		throw new InternalServerException(ErrorMessage.ACCOUNT_NUMBER_GENERATION_FAILED);
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

	private void retryAccountCreation(Member member, OpenAccountRequest openAccountRequest, AccountType accountType,
		String encodedPassword) {

		for (int i = 0; true; i++) {
			final String accountNumber = accountType.getCode() + generateRandomAccountNumber();

			try {
				final Account account = Account.createAccount(
					member, accountNumber, openAccountRequest.accountName(), encodedPassword, accountType);
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
		final OutboxMessageRequest outboxMessageRequest = EventMapper.toOutboxMessageRequest(
			account, eventType, eventMessage);
		outboxMessageHandler.recordOutboxMessage(outboxMessageRequest);
	}
}
