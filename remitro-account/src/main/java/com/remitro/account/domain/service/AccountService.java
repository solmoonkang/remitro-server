package com.remitro.account.domain.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.dto.request.OpenAccountRequest;
import com.remitro.account.application.dto.response.OpenAccountCreationResponse;
import com.remitro.account.application.mapper.AccountMapper;
import com.remitro.account.application.validator.AccountValidator;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.OutboxMessage;
import com.remitro.account.domain.model.enums.AccountType;
import com.remitro.account.domain.model.read.MemberProjection;
import com.remitro.account.domain.repository.AccountRepository;
import com.remitro.account.domain.repository.MemberProjectionRepository;
import com.remitro.account.domain.repository.OutboxMessageRepository;
import com.remitro.common.contract.account.AccountOpenedEvent;
import com.remitro.common.domain.enums.AggregateType;
import com.remitro.common.domain.enums.EventType;
import com.remitro.common.infra.error.exception.InternalServerException;
import com.remitro.common.infra.error.exception.NotFoundException;
import com.remitro.common.infra.error.model.ErrorMessage;
import com.remitro.common.infra.util.JsonUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

	private static final int MAX_ACCOUNT_NUMBER_ATTEMPTS = 5;
	private static final DateTimeFormatter ACCOUNT_NUMBER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

	private final AccountValidator accountValidator;
	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;
	private final MemberProjectionRepository memberProjectionRepository;
	private final OutboxMessageRepository outboxMessageRepository;

	@Transactional
	public OpenAccountCreationResponse openAccount(Long memberId, OpenAccountRequest openAccountRequest) {
		final MemberProjection member = memberProjectionRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND));

		accountValidator.validateMemberIsActive(member);

		final String accountNumber = generateUniqueAccountNumber(openAccountRequest.accountType());
		final String encodedPassword = passwordEncoder.encode(openAccountRequest.password());
		final Account account = Account.create(
			memberId, accountNumber, openAccountRequest.accountName(), encodedPassword, openAccountRequest.accountType()
		);
		accountRepository.save(account);

		final AccountOpenedEvent accountOpendEvent = new AccountOpenedEvent(account.getId(), account.getMemberId(), account.getAccountType().getCode());
		final String eventMessage = JsonUtil.toJSON(accountOpendEvent);
		final OutboxMessage outboxMessage = OutboxMessage.create(account.getId(), AggregateType.ACCOUNT, EventType.ACCOUNT_OPENED, eventMessage);
		outboxMessageRepository.save(outboxMessage);

		return AccountMapper.toOpenAccountCreationResponse(account);
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
