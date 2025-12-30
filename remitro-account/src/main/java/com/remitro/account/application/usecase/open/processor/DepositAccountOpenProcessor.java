package com.remitro.account.application.usecase.open.processor;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.account.application.usecase.open.command.AccountOpenCommand;
import com.remitro.account.application.usecase.open.command.DepositAccountOpenCommand;
import com.remitro.account.application.usecase.open.processor.context.OpenedAccountContext;
import com.remitro.account.application.common.support.generator.AccountNumberGenerator;
import com.remitro.account.domain.product.enums.ProductType;
import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.member.model.MemberProjection;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DepositAccountOpenProcessor implements AccountOpenProcessor {

	private final AccountNumberGenerator accountNumberGenerator;

	@Override
	public ProductType supportedProductType() {
		return ProductType.CHECKING;
	}

	@Override
	public OpenedAccountContext open(
		MemberProjection member,
		AccountOpenCommand accountOpenCommand,
		LocalDateTime now
	) {
		final DepositAccountOpenCommand depositAccountOpenCommand = (DepositAccountOpenCommand)accountOpenCommand;

		final Account account = Account.create(
			member.getMemberId(),
			accountNumberGenerator.generate(accountOpenCommand.productType()),
			depositAccountOpenCommand.accountName(),
			depositAccountOpenCommand.encodedPinNumber(),
			accountOpenCommand.productType()
		);

		return OpenedAccountContext.accountOnly(account);
	}
}
