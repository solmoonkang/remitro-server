package com.remitro.account.application.common.mapper;

import java.time.LocalDateTime;

import com.remitro.account.domain.common.enums.ActorType;
import com.remitro.account.domain.product.enums.ProductType;
import com.remitro.account.domain.account.enums.StatusChangeReason;
import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.loan.model.Loan;
import com.remitro.event.domain.account.AccountOpenedEvent;
import com.remitro.event.domain.account.enums.AccountActorType;
import com.remitro.event.domain.account.enums.AccountProductType;
import com.remitro.event.domain.account.enums.AccountStatusChangeReason;
import com.remitro.event.domain.loan.LoanCreatedEvent;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountEventMapper {

	private static AccountProductType mapProductType(ProductType productType) {
		return AccountProductType.valueOf(productType.name());
	}

	private static AccountActorType mapActorType(ActorType actorType) {
		return AccountActorType.valueOf(actorType.name());
	}

	private static AccountStatusChangeReason mapAccountStatusUpdateReason(StatusChangeReason statusChangeReason) {
		return AccountStatusChangeReason.valueOf(statusChangeReason.name());
	}

	public static AccountOpenedEvent toAccountOpenedEvent(
		Account account,
		ActorType actorType,
		StatusChangeReason statusChangeReason,
		LocalDateTime now
	) {
		return new AccountOpenedEvent(
			account.getId(),
			account.getMemberId(),
			mapProductType(account.getProductType()),
			mapActorType(actorType),
			mapAccountStatusUpdateReason(statusChangeReason),
			now
		);
	}

	public static LoanCreatedEvent toLoanCreatedEvent(Loan loan, LocalDateTime occurredAt) {
		return new LoanCreatedEvent(
			loan.getId(),
			loan.getAccountId(),
			loan.getPrincipalAmount(),
			loan.getInterestRate(),
			occurredAt
		);
	}
}
