package com.remitro.account.application.usecase.open.command;

import com.remitro.account.domain.product.enums.ProductType;

public record DepositAccountOpenCommand(
	Long memberId,

	String accountName,

	String encodedPinNumber,

	ProductType productType

) implements AccountOpenCommand {
}
