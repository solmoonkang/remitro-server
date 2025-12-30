package com.remitro.account.application.usecase.open.processor;

import java.time.LocalDateTime;

import com.remitro.account.application.usecase.open.command.AccountOpenCommand;
import com.remitro.account.application.usecase.open.processor.context.OpenedAccountContext;
import com.remitro.account.domain.product.enums.ProductType;
import com.remitro.account.domain.member.model.MemberProjection;

public interface AccountOpenProcessor {

	ProductType supportedProductType();

	OpenedAccountContext open(MemberProjection member, AccountOpenCommand accountOpenCommand, LocalDateTime now);
}
