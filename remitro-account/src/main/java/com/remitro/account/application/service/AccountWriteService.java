package com.remitro.account.application.service;

import static com.remitro.account.domain.constant.AccountConstant.*;

import java.time.LocalDateTime;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.remitro.account.application.dto.request.OpenAccountRequest;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.MemberProjection;
import com.remitro.account.domain.model.OutboxMessage;
import com.remitro.account.domain.model.enums.AccountType;
import com.remitro.account.domain.repository.AccountRepository;
import com.remitro.account.domain.repository.OutboxMessageRepository;
import com.remitro.common.contract.account.AccountOpenedEvent;
import com.remitro.common.domain.enums.AggregateType;
import com.remitro.common.domain.enums.EventType;
import com.remitro.common.infrastructure.error.exception.InternalServerException;
import com.remitro.common.infrastructure.error.model.ErrorMessage;
import com.remitro.common.infrastructure.util.JsonMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountWriteService {

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
		final String eventMessage = JsonMapper.toJSON(accountOpendEvent);

		final OutboxMessage outboxMessage = OutboxMessage.create(
			account.getId(),
			AggregateType.ACCOUNT,
			EventType.ACCOUNT_OPENED,
			eventMessage
		);

		outboxMessageRepository.save(outboxMessage);
	}

	public void increaseBalance(Account account, Long amount) {
		account.increaseBalance(amount);
	}

	private String generateUniqueAccountNumber(AccountType accountType) {
		for (int attempt = 0; attempt < ACCOUNT_NUMBER_GENERATION_MAX_ATTEMPTS; attempt++) {
			String accountNumber = accountType.getCode()
				+ LocalDateTime.now().format(ACCOUNT_NUMBER_GENERATION_FORMAT)
				+ RandomStringUtils.randomNumeric(5);

			if (!accountRepository.existsByAccountNumber(accountNumber))
				return accountNumber;
		}

		throw new InternalServerException(ErrorMessage.ACCOUNT_NUMBER_GENERATION_FAILED);
	}
}
