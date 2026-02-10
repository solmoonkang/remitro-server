package com.remitro.account.application.command.account;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.command.account.validator.AccountOpenValidator;
import com.remitro.account.application.command.account.validator.PinNumberValidator;
import com.remitro.account.application.command.dto.request.AccountOpenRequest;
import com.remitro.account.application.command.dto.response.AccountOpenResponse;
import com.remitro.account.application.idempotency.IdempotencyProvider;
import com.remitro.account.application.mapper.AccountMapper;
import com.remitro.account.application.read.MemberFinder;
import com.remitro.account.application.support.AccountNumberGenerator;
import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.account.policy.FormatPolicy;
import com.remitro.account.domain.account.repository.AccountRepository;
import com.remitro.account.domain.projection.model.MemberProjection;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountOpenCommandService {

	private final MemberFinder memberFinder;
	private final AccountRepository accountRepository;
	private final IdempotencyProvider idempotencyProvider;

	private final AccountNumberGenerator accountNumberGenerator;
	private final AccountOpenValidator accountOpenValidator;
	private final PinNumberValidator pinNumberValidator;
	private final FormatPolicy formatPolicy;
	private final PasswordEncoder passwordEncoder;

	public AccountOpenResponse open(Long memberId, String requestId, AccountOpenRequest accountOpenRequest) {
		idempotencyProvider.process(requestId, memberId);

		final MemberProjection member = memberFinder.getMemberById(memberId);

		accountOpenValidator.validateMemberOpenable(member);
		accountOpenValidator.validateAccountTypeOpenable(accountOpenRequest.accountType());
		accountOpenValidator.validateAccountOpenLimit(memberId, accountOpenRequest.accountType());
		pinNumberValidator.validatePinConfirm(accountOpenRequest.pinNumber(), accountOpenRequest.confirmPinNumber());

		final String accountNumber = accountNumberGenerator.generate(accountOpenRequest.accountType());
		final String accountAlias = accountOpenValidator.trimAccountAlias(accountOpenRequest.accountAlias());
		final String pinNumberHash = passwordEncoder.encode(accountOpenRequest.pinNumber());

		final Account account = Account.open(
			memberId,
			accountNumber,
			accountAlias,
			accountOpenRequest.accountType(),
			pinNumberHash
		);
		accountRepository.save(account);

		return AccountMapper.toAccountOpenResponse(
			formatPolicy.formatAccountNumber(account.getAccountNumber()),
			account.getAccountAlias(),
			account.getCreatedAt()
		);
	}
}
