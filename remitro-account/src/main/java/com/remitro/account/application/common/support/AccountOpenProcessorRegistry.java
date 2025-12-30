package com.remitro.account.application.common.support;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.remitro.account.application.usecase.open.processor.AccountOpenProcessor;
import com.remitro.account.domain.product.enums.ProductType;
import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.message.ErrorMessage;

@Component
public class AccountOpenProcessorRegistry {

	private final Map<ProductType, AccountOpenProcessor> accountOpenProcessorMap;

	public AccountOpenProcessorRegistry(List<AccountOpenProcessor> accountOpenProcessors) {
		this.accountOpenProcessorMap = accountOpenProcessors.stream()
			.collect(Collectors.toMap(
				AccountOpenProcessor::supportedProductType,
				Function.identity()
			));
	}

	public AccountOpenProcessor get(ProductType productType) {
		return Optional.ofNullable(accountOpenProcessorMap.get(productType))
			.orElseThrow(() -> new BadRequestException(
				ErrorCode.UNSUPPORTED_ACCOUNT_PRODUCT_TYPE, ErrorMessage.UNSUPPORTED_ACCOUNT_PRODUCT_TYPE)
			);
	}
}
