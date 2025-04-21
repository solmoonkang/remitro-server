package com.remitroserver.api.application.account;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.remitroserver.api.domain.account.entity.Account;
import com.remitroserver.api.domain.account.model.AccountType;
import com.remitroserver.api.domain.account.repository.AccountRepository;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.global.common.util.AccountNumberGenerator;
import com.remitroserver.global.error.exception.BadRequestException;
import com.remitroserver.global.error.exception.ConflictException;
import com.remitroserver.global.error.exception.NotFoundException;
import com.remitroserver.global.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountReadService {

	private static final int MAX_ACCOUNT_NUMBER_GENERATION_RETRY = 5;

	private final AccountNumberGenerator accountNumberGenerator;
	private final AccountRepository accountRepository;

	public String generateUniqueAccountNumber(AccountType accountType) {
		int attemptCount = 0;

		while (attemptCount++ < MAX_ACCOUNT_NUMBER_GENERATION_RETRY) {
			final String generatedNumber = accountNumberGenerator.generateAccountNumber(accountType);

			if (!accountRepository.existsByAccountNumber(generatedNumber)) {
				return generatedNumber;
			}
		}

		throw new ConflictException(ErrorMessage.ACCOUNT_NUMBER_GENERATION_FAILED_ERROR);
	}

	public List<Account> getAccountsByMember(Member member) {
		return accountRepository.findAccountsByMemberOrderByCreatedAt(member);
	}

	public Account getAccountByTokenAndOwner(UUID accountToken, Member member) {
		return accountRepository.findByAccountTokenAndMember(accountToken, member)
			.orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND_ERROR));
	}

	public void validateAccountLimitExceeded(Member member, AccountType accountType) {
		if (accountRepository.countByMemberAndAccountType(member, accountType) >= accountType.getMaxAccount()) {
			throw new BadRequestException(ACCOUNT_TYPE_LIMIT_EXCEEDED_ERROR);
		}
	}
}
