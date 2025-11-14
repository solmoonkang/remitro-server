package com.remitro.account.application.validator;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.remitro.account.domain.model.read.MemberProjection;
import com.remitro.account.domain.repository.AccountRepository;
import com.remitro.common.contract.member.ActivityStatus;
import com.remitro.common.infra.error.exception.BadRequestException;
import com.remitro.common.infra.error.exception.ConflictException;
import com.remitro.common.infra.error.exception.ForbiddenException;
import com.remitro.common.infra.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountValidator {

	public static final long MINIMUM_AMOUNT = 0L;

	private final PasswordEncoder passwordEncoder;
	private final AccountRepository accountRepository;

	public void validateAccountAccess(Long accountOwnerId, Long loginMemberId) {
		if (!accountOwnerId.equals(loginMemberId)) {
			throw new ForbiddenException(ErrorMessage.ACCOUNT_ACCESS_FORBIDDEN);
		}
	}

	public void validateAccountPasswordMatch(String rawPassword, String savedPassword) {
		if (!passwordEncoder.matches(rawPassword, savedPassword)) {
			throw new BadRequestException(ErrorMessage.INVALID_PASSWORD);
		}
	}

	public void validateAmountPositive(Long amount) {
		if (amount <= MINIMUM_AMOUNT) {
			throw new BadRequestException(ErrorMessage.INVALID_AMOUNT);
		}
	}

	public void validateSufficientBalance(Long balance, Long amount) {
		if (balance < amount) {
			throw new BadRequestException(ErrorMessage.INSUFFICIENT_FUNDS);
		}
	}

	public void validateSelfTransfer(Long senderAccountId, Long receiverAccountId) {
		if (senderAccountId.equals(receiverAccountId)) {
			throw new BadRequestException(ErrorMessage.INVALID_TRANSFER);
		}
	}

	public void validateMemberIsActive(MemberProjection memberProjection) {
		if (memberProjection.getActivityStatus() != ActivityStatus.ACTIVE) {
			throw new ConflictException(ErrorMessage.MEMBER_INACTIVE);
		}
	}
}
