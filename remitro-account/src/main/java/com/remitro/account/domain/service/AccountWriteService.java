package com.remitro.account.domain.service;

import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.remitro.account.application.dto.request.CreateAccountRequest;
import com.remitro.account.application.dto.request.UpdateStatusRequest;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.enums.AccountStatus;
import com.remitro.account.domain.model.enums.AccountType;
import com.remitro.account.domain.repository.AccountRepository;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountWriteService {

	public static final int RANDOM_ACCOUNT_NUMBER_LENGTH = 8;

	private final PasswordEncoder passwordEncoder;
	private final AccountRepository accountRepository;

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

	private String generateRandomAccountNumber() {
		Random random = new Random();
		StringBuilder randomNumberBuilder = new StringBuilder();

		for (int i = 0; i < RANDOM_ACCOUNT_NUMBER_LENGTH; i++) {
			randomNumberBuilder.append(random.nextInt(10));
		}

		return randomNumberBuilder.toString();
	}
}
