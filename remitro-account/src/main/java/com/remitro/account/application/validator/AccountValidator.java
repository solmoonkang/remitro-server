package com.remitro.account.application.validator;

import static com.remitro.account.infrastructure.constant.AccountConstant.*;
import static com.remitro.account.domain.enums.AccountStatus.*;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.MemberProjection;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.exception.ForbiddenException;
import com.remitro.common.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountValidator {

	private final PasswordEncoder passwordEncoder;

	public void validateMemberIsActive(MemberProjection memberProjection) {
		if (!memberProjection.isAccountOpenAllowed()) {
			throw new ConflictException(ErrorMessage.MEMBER_NOT_ELIGIBLE);
		}
	}

	public void validateAccountOwner(Long accountOwnerId, Long loginMemberId) {
		if (!Objects.equals(accountOwnerId, loginMemberId)) {
			throw new ForbiddenException(ErrorMessage.ACCOUNT_ACCESS_FORBIDDEN);
		}
	}

	public void validateAccountStatusForDeposit(Account account) {
		if (account.getAccountStatus() == TERMINATED || account.getAccountStatus() == PENDING_TERMINATION) {
			throw new BadRequestException(ErrorMessage.ACCOUNT_TERMINATED);
		}

		if (account.getAccountStatus() == SUSPENDED) {
			throw new BadRequestException(ErrorMessage.ACCOUNT_SUSPENDED);
		}
	}

	public void validateAccountStatusForWithdraw(Account account) {
		if (account.getAccountStatus() != NORMAL) {
			throw new BadRequestException(ErrorMessage.ACCOUNT_NOT_ACTIVE);
		}
	}

	public void validateAccountStatusForTransfer(Account account) {
		if (account.getAccountStatus() != NORMAL) {
			throw new BadRequestException(ErrorMessage.ACCOUNT_NOT_ACTIVE);
		}
	}

	public void validateAmountPositive(Long amount) {
		if (amount == null || amount <= MINIMUM_AMOUNT) {
			throw new BadRequestException(ErrorMessage.INVALID_AMOUNT);
		}
	}

	public void validateSufficientBalance(Account account, Long amount) {
		if (account.getBalance() < amount) {
			throw new BadRequestException(ErrorMessage.INSUFFICIENT_FUNDS);
		}
	}

	public void validateAccountPasswordMatch(String rawPassword, String savedPassword) {
		if (!passwordEncoder.matches(rawPassword, savedPassword)) {
			throw new BadRequestException(ErrorMessage.INVALID_PASSWORD);
		}
	}
}
