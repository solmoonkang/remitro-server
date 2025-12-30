package com.remitro.account.application.common.validator;

import org.springframework.stereotype.Component;

import com.remitro.account.application.usecase.open.command.AccountOpenCommand;
import com.remitro.account.application.usecase.open.command.LoanAccountOpenCommand;
import com.remitro.account.application.usecase.open.policy.AccountCountPolicy;
import com.remitro.account.application.usecase.open.policy.LoanAccountPolicy;
import com.remitro.account.application.usecase.open.policy.ProductAccountLimitPolicy;
import com.remitro.account.domain.member.model.MemberProjection;
import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.message.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountValidator {

	private final AccountCountPolicy accountCountPolicy;
	private final ProductAccountLimitPolicy productAccountLimitPolicy;
	private final LoanAccountPolicy loanAccountPolicy;

	public void validateAccountOpenAllowed(MemberProjection member, AccountOpenCommand accountOpenCommand) {
		if (!member.isAccountOpenAllowed()) {
			throw new ConflictException(ErrorCode.MEMBER_NOT_ELIGIBLE, ErrorMessage.MEMBER_NOT_ELIGIBLE);
		}

		accountCountPolicy.validateMaxAccountCount(member.getMemberId());

		productAccountLimitPolicy.validate(accountOpenCommand.productType(), member.getMemberId());

		if (accountOpenCommand instanceof LoanAccountOpenCommand) {
			loanAccountPolicy.validateLoanOpenAllowed(member.getMemberId());
		}
	}
}
