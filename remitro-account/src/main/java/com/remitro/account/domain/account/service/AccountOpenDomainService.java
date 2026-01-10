package com.remitro.account.domain.account.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.remitro.account.domain.account.enums.ProductType;
import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.account.generator.AccountNumberGenerator;
import com.remitro.account.domain.account.policy.PinPolicy;
import com.remitro.account.domain.account.repository.AccountCommandRepository;
import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.message.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountOpenDomainService {

	private static final int MAX_RETRY = 3;

	private final AccountCommandRepository accountCommandRepository;
	private final AccountNumberGenerator accountNumberGenerator;
	private final PinPolicy pinPolicy;

	public Account open(Long memberId, String accountName, String pin, ProductType productType) {
		for (int attempt = 0; attempt < MAX_RETRY; attempt++) {
			final String accountNumber = accountNumberGenerator.generate(productType);

			try {
				final Account account = Account.create(
					memberId,
					accountNumber,
					accountName,
					pinPolicy.encode(pin),
					productType
				);
				accountCommandRepository.save(account);

			} catch (DataIntegrityViolationException e) {
				if (attempt == MAX_RETRY - 1) {
					throw new ConflictException(
						ErrorCode.ACCOUNT_NUMBER_DUPLICATED, ErrorMessage.DUPLICATE_REQUEST
					);
				}
			}
		}

		throw new ConflictException(
			ErrorCode.ACCOUNT_NUMBER_DUPLICATED, ErrorMessage.DUPLICATE_REQUEST
		);
	}
}
