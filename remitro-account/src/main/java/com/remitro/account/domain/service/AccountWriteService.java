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

	private static final int MAX_ACCOUNT_NUMBER_ATTEMPTS = 5;
	private static final DateTimeFormatter ACCOUNT_NUMBER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

	private final PasswordEncoder passwordEncoder;
	private final AccountRepository accountRepository;
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
}
