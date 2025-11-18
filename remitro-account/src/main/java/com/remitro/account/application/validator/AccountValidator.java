package com.remitro.account.application.validator;

import static com.remitro.account.domain.constant.AccountConstant.*;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.model.MemberProjection;
import com.remitro.account.domain.model.enums.AccountStatus;
import com.remitro.common.contract.member.ActivityStatus;
import com.remitro.common.infra.error.exception.BadRequestException;
import com.remitro.common.infra.error.exception.ConflictException;
import com.remitro.common.infra.error.exception.ForbiddenException;
import com.remitro.common.infra.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountValidator {

	private final PasswordEncoder passwordEncoder;

	public void validateMemberIsActive(MemberProjection memberProjection) {
		if (memberProjection.getActivityStatus() != ActivityStatus.ACTIVE) {
			throw new ConflictException(ErrorMessage.MEMBER_INACTIVE);
		}
	}

	public void validateAccountOwner(Long accountOwnerId, Long loginMemberId) {
		if (!Objects.equals(accountOwnerId, loginMemberId)) {
			throw new ForbiddenException(ErrorMessage.ACCOUNT_ACCESS_FORBIDDEN);
		}
	}

	public void validateAccountStatusNormal(AccountStatus accountStatus) {
		if (accountStatus != AccountStatus.NORMAL) {
			throw new BadRequestException(ErrorMessage.INACTIVE_ACCOUNT_STATUS);
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
