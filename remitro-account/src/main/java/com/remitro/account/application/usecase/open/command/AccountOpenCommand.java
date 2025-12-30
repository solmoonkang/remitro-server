package com.remitro.account.application.usecase.open.command;

import com.remitro.account.domain.product.enums.ProductType;

public sealed interface AccountOpenCommand permits
	DepositAccountOpenCommand,
	LoanAccountOpenCommand,
	VirtualAccountOpenCommand {

	ProductType productType();
}
