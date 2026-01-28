package com.remitro.account.application.command.deposit;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.command.dto.request.DepositOpenRequest;
import com.remitro.account.application.command.dto.response.DepositOpenResponse;
import com.remitro.account.application.mapper.AccountMapper;
import com.remitro.account.application.support.AccountNumberGenerator;
import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.account.policy.FormatPolicy;
import com.remitro.account.domain.account.repository.AccountRepository;
import com.remitro.account.domain.idempotency.model.Idempotency;
import com.remitro.account.domain.idempotency.repository.IdempotencyRepository;
import com.remitro.account.domain.projection.model.MemberProjection;
import com.remitro.account.domain.projection.repository.MemberProjectionRepository;
import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.BadRequestException;
import com.remitro.support.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DepositOpenCommandService {

	private final AccountRepository accountRepository;
	private final MemberProjectionRepository memberProjectionRepository;
	private final IdempotencyRepository idempotencyRepository;

	private final AccountNumberGenerator accountNumberGenerator;
	private final DepositOpenValidator depositOpenValidator;
	private final PinNumberValidator pinNumberValidator;
	private final FormatPolicy formatPolicy;

	private final PasswordEncoder passwordEncoder;
	private final Clock clock;

	public DepositOpenResponse open(Long memberId, String requestId, DepositOpenRequest depositOpenRequest) {
		boolean acquired = idempotencyRepository.saveIfAbsent(
			Idempotency.issue(requestId, memberId, LocalDateTime.now(clock))
		);
		validateIdempotencyAcquired(acquired);

		final MemberProjection member = memberProjectionRepository.findByMemberId(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		depositOpenValidator.validateMember(member);
		depositOpenValidator.validateLimit(memberId);
		pinNumberValidator.validateConfirm(depositOpenRequest.pinNumber(), depositOpenRequest.confirmPinNumber());

		final String accountNumber = accountNumberGenerator.generate(depositOpenRequest.accountType());
		final String pinNumberHash = passwordEncoder.encode(depositOpenRequest.pinNumber());

		final Account account = Account.open(
			memberId,
			accountNumber,
			depositOpenRequest.accountAlias(),
			depositOpenRequest.accountType(),
			pinNumberHash
		);
		accountRepository.save(account);

		return AccountMapper.toDepositOpenResponse(
			formatPolicy.format(accountNumber),
			depositOpenRequest.accountAlias(),
			account.getCreatedAt()
		);
	}

	private void validateIdempotencyAcquired(boolean acquired) {
		if (!acquired) {
			throw new BadRequestException(ErrorCode.DUPLICATE_RESOURCE);
		}
	}
}
